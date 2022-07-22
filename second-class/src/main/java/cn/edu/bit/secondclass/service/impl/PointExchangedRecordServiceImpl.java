package cn.edu.bit.secondclass.service.impl;

import cn.edu.bit.secondclass.domain.PointChangedRecord;
import cn.edu.bit.secondclass.domain.PointExchangedRecord;
import cn.edu.bit.secondclass.domain.Product;
import cn.edu.bit.secondclass.domain.User;
import cn.edu.bit.secondclass.exception.PointExchangedRecordDaoException;
import cn.edu.bit.secondclass.myenum.PointExchangedRecordStatus;
import cn.edu.bit.secondclass.repository.PointChangedRecordRepository;
import cn.edu.bit.secondclass.repository.PointExchangedRecordRepository;
import cn.edu.bit.secondclass.repository.ProductRepository;
import cn.edu.bit.secondclass.repository.UserRepository;
import cn.edu.bit.secondclass.service.PointExchangedRecordService;
import cn.edu.bit.secondclass.vo.PointExchangedRecordInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;

@Service
public class PointExchangedRecordServiceImpl implements PointExchangedRecordService {
    @Autowired
    private PointExchangedRecordRepository pointExchangedRecordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PointChangedRecordRepository pointChangedRecordRepository;

    @Autowired
    private ProductRepository productRepository;

    private final ReadWriteLock readWriteLock;
    private final Lock readLock;
    private final Lock writeLock;

    @Autowired
    public PointExchangedRecordServiceImpl(ReadWriteLock readWriteLock) {
        this.readWriteLock = readWriteLock;
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
    }


    @Override
    public List<PointExchangedRecordInfoVo> getAllPointExchangedRecords(String status) {
        if (status == null || status.equals("")) {
            return pointExchangedRecordRepository
                    .findAll()
                    .stream()
                    .map(PointExchangedRecordInfoVo::convertToVo)
                    .collect(Collectors.toList());
        } else {
            return pointExchangedRecordRepository
                    .findPointExchangedRecordsByStatus(status)
                    .stream()
                    .map(PointExchangedRecordInfoVo::convertToVo)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<PointExchangedRecordInfoVo> getPointExchangedRecordByUserIdAndStatus(String schoolId, String status) {
        if (status == null) {
            return pointExchangedRecordRepository
                    .findPointExchangedRecordsBySchoolId(schoolId)
                    .stream()
                    .map(PointExchangedRecordInfoVo::convertToVo)
                    .collect(Collectors.toList());
        } else {
            return pointExchangedRecordRepository
                    .findPointExchangedRecordsBySchoolIdAndStatus(schoolId, status)
                    .stream()
                    .map(PointExchangedRecordInfoVo::convertToVo)
                    .collect(Collectors.toList());
        }

    }

    @Override
    public void checkout(int recordId, String status, Integer adminId, String checkNote) {
        if (adminId == null) {
            throw new PointExchangedRecordDaoException("审批人不可为空");
        }

        if (checkNote == null) {
            checkNote = "";
        }

        PointExchangedRecord record = pointExchangedRecordRepository.findPointExchangedRecordById(recordId);

        if (record == null) {
            throw new PointExchangedRecordDaoException("不存在该兑换记录");
        }

        if (record.getStatus().equals(PointExchangedRecordStatus.FINISH.getStatus())) {
            throw new PointExchangedRecordDaoException("该兑换记录已完成，无法审批");
        }

        if (record.getStatus().equals(PointExchangedRecordStatus.CANCEL.getStatus())) {
            throw new PointExchangedRecordDaoException("该兑换记录已被取消，无法审批");
        }

        if (record.getStatus().equals(PointExchangedRecordStatus.REJECT.getStatus())) {
            throw new PointExchangedRecordDaoException("该兑换记录已被拒绝，无法审批");
        }

        //TODO:目前用大锁，后续细化锁的粒度
        writeLock.lock();
        try {
            User user = userRepository.findUserBySchoolId(record.getSchoolId());
            Product product = productRepository.findProductById(record.getProduct());
            if (product.getStock() <= 0) {
                throw new PointExchangedRecordDaoException("该产品当前无库存，无法兑换");
            }

            int point = user.getPoint();
            if (point < product.getPoint()) {
                throw new PointExchangedRecordDaoException("用户积分不足以兑换该产品，无法审批");
            }

            // 修改用户积分
            user.setPoint(point - product.getPoint());
            userRepository.save(user);

            // 修改商品库存
            product.setStock(product.getStock() - 1);
            productRepository.save(product);


            // 修改积分兑换记录
            record.setStatus(status);
            record.setAdmin(adminId);
            record.setCheckNote(checkNote);
            record = pointExchangedRecordRepository.save(record);

            // 生成积分变动记录
            PointChangedRecord changedRecord = makePointChangedRecord(record, -product.getPoint());
            pointChangedRecordRepository.save(changedRecord);

        } finally {
            writeLock.unlock();
        }


    }

    private static PointChangedRecord makePointChangedRecord(PointExchangedRecord exchangedRecord, int point) {
        PointChangedRecord changedRecord = new PointChangedRecord();
        changedRecord.setAdmin(exchangedRecord.getAdmin());
        changedRecord.setReason(exchangedRecord.getCheckNote());
        changedRecord.setTransaction(exchangedRecord.getId());
        changedRecord.setSchoolId(exchangedRecord.getSchoolId());
        changedRecord.setVariation(point);
        changedRecord.setDate(new Date());

        return changedRecord;
    }
}