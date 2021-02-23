package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.community.domain.Appointment;
import cn.edu.bit.ruixin.community.exception.AppointmentDaoException;
import cn.edu.bit.ruixin.community.repository.AppointmentRepository;
import cn.edu.bit.ruixin.community.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/6
 */
@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Appointment> getAllAppointmentByUsername(String username) {
        return appointmentRepository.findAllByLauncher(username);
    }

    @Transactional(readOnly = true)
    @Override
    public Appointment getAppointmentById(Integer id) {
        return appointmentRepository.findAppointmentById(id);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @Override
    public void addANewAppointment(Appointment appointment) {
        // 判断该房间所预约时间段是否空闲
        Integer roomId = appointment.getRoomId();
        Integer launchTime = appointment.getLaunchTime();
        Appointment getAppointment = appointmentRepository.findAppointmentByRoomIdEqualsAndLaunchTimeEqualsAndStatusEquals(roomId, launchTime, "receive");
        if (getAppointment != null) {
            throw new AppointmentDaoException("该房间此时间段已被预约!");
        } else {
            appointment.setStatus("new");
            appointmentRepository.save(appointment);
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @Override
    public void cancelAppointmentById(Integer id) {
        Appointment appointment = appointmentRepository.findAppointmentById(id);
        if (appointment != null) {
            if (appointment.getStatus().equals("receive")) {
                throw new AppointmentDaoException("该预约已审批通过，不可撤销!");
            }
            if (appointment.getStatus().equals("reject")) {
                throw new AppointmentDaoException("该预约已被审批驳回，不可撤销!");
            }
            if (appointment.getStatus().equals("cancel")) {
                throw new AppointmentDaoException("该预约已被撤销，不可重复撤销!");
            }
            // 撤销预约
            appointment.setStatus("cancel");
            appointmentRepository.save(appointment);
        } else {
            throw new AppointmentDaoException("不存在该预约记录!");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Appointment> getAllAppointment(String status) {
        if (status != null && !status.equals("")) {
            return appointmentRepository.findAllByStatusEquals(status);
        } else {
            return appointmentRepository.findAll();
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @Override
    public void checkOutAppointment(Integer id, String status, String conductor) {
        Appointment appointment = appointmentRepository.findAppointmentById(id);
        if (appointment != null) {
            if (appointment.getStatus().equals("cancel")) {
                throw new AppointmentDaoException("该预约已经取消，无法审批!");
            }
            if (appointment.getStatus().equals("receive")) {
                throw new AppointmentDaoException("该预约已审批通过，不可重复审批!");
            }
            if (appointment.getStatus().equals("reject")) {
                throw new AppointmentDaoException("该预约已审批驳回，不可重复审批!");
            }
            if (conductor != null && !conductor.equals("")) {
                appointment.setStatus(status);
                appointment.setConductor(conductor);
                appointmentRepository.save(appointment);
            } else {
                throw new AppointmentDaoException("审批人姓名不可为空!");
            }
        }
    }
}
