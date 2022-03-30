package cn.edu.bit.secondclass.service.impl;

import cn.edu.bit.secondclass.domain.Admin;
import cn.edu.bit.secondclass.domain.ClassHourChangedRecord;
import cn.edu.bit.secondclass.domain.PointChangedRecord;
import cn.edu.bit.secondclass.domain.User;
import cn.edu.bit.secondclass.exception.ClassHourChangedRecordDaoException;
import cn.edu.bit.secondclass.repository.AdminRepository;
import cn.edu.bit.secondclass.repository.ClassHourChangedRecordRepository;
import cn.edu.bit.secondclass.repository.PointChangedRecordRepository;
import cn.edu.bit.secondclass.repository.UserRepository;
import cn.edu.bit.secondclass.service.ClassHourChangedRecordService;
import cn.edu.bit.secondclass.vo.ClassHourChangedRecordInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;

@Service
public class ClassHourChangedRecordServiceImpl implements ClassHourChangedRecordService {
    @Autowired
    private ClassHourChangedRecordRepository classHourChangedRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PointChangedRecordRepository pointChangedRecordRepository;


    private final ReadWriteLock readWriteLock;
    private final Lock readLock;
    private final Lock writeLock;

    @Autowired
    public ClassHourChangedRecordServiceImpl(ReadWriteLock readWriteLock) {
        this.readWriteLock = readWriteLock;
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public void updateUserClassHour(List<ClassHourChangedRecordInfoVo> recordInfoVos, Integer adminId, String reason) {
        if (adminId == null) {
            throw new ClassHourChangedRecordDaoException("审批者不能为空");
        }

        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ClassHourChangedRecordDaoException("该审批者不存在");
        }

        if (reason == null) {
            reason = "";
        }

        List<ClassHourChangedRecord> records = recordInfoVos.stream()
                .map(ClassHourChangedRecordServiceImpl::convertVoToRecord)
                .collect(Collectors.toList());

        classHourChangedRecordRepository.saveAll(records);

        //TODO:细化锁的粒度
        writeLock.lock();
        try {
            // 修改用户积分与学时
            List<User> users = records.stream()
                    .map(record -> {
                        User user = userRepository.findUserBySchoolId(record.getSchoolId());
                        user.setClassHour(user.getClassHour() + record.getVariation());
                        user.setPoint(user.getPoint() + (int)(record.getVariation() * 10));
                        return user;
                    }).collect(Collectors.toList());
            userRepository.saveAll(users);

            // 生成积分变动记录
            String finalReason = reason;
            List<PointChangedRecord> changedRecordList = records.stream()
                    .map(record -> ClassHourChangedRecordServiceImpl.makePointChangedRecord(record, adminId, finalReason))
                    .collect(Collectors.toList());
            pointChangedRecordRepository.saveAll(changedRecordList);
        } finally {
            writeLock.unlock();
        }
    }

    private static ClassHourChangedRecord convertVoToRecord(ClassHourChangedRecordInfoVo infoVo) {
        ClassHourChangedRecord record = new ClassHourChangedRecord();
        record.setId(infoVo.getId());
        record.setActivity(infoVo.getActivity());
        record.setVariation(infoVo.getVariation());
        record.setSchoolId(infoVo.getSchoolId());
        record.setDate(new Date(infoVo.getDate()));
        return record;
    }

    private static PointChangedRecord makePointChangedRecord(ClassHourChangedRecord record,
                                                             int adminId, String reason) {
        PointChangedRecord changedRecord = new PointChangedRecord();
        changedRecord.setTransaction(null);
        changedRecord.setDate(new Date());
        changedRecord.setSchoolId(record.getSchoolId());
        changedRecord.setVariation((int)(record.getVariation() * 10));
        changedRecord.setAdmin(adminId);
        changedRecord.setReason(reason);
        return changedRecord;
    }
}
