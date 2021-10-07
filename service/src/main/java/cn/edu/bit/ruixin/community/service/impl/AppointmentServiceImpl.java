package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.community.domain.Admin;
import cn.edu.bit.ruixin.community.domain.Appointment;
import cn.edu.bit.ruixin.community.domain.Room;
import cn.edu.bit.ruixin.community.domain.Schedule;
import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.domain.WxMsgTemplateVo;
import cn.edu.bit.ruixin.community.exception.AppointmentDaoException;
import cn.edu.bit.ruixin.community.myenum.AppointmentStatus;
import cn.edu.bit.ruixin.community.repository.AdminRepository;
import cn.edu.bit.ruixin.community.repository.AppointmentRepository;
import cn.edu.bit.ruixin.community.repository.RoomsRepository;
import cn.edu.bit.ruixin.community.repository.ScheduleRepository;
import cn.edu.bit.ruixin.community.repository.UserRepository;
import cn.edu.bit.ruixin.community.service.AppointmentService;
import cn.edu.bit.ruixin.community.service.WechatService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private WechatService wechatService;

    @Autowired
    private RoomsRepository roomsRepository;

    private Log logger = LogFactory.getLog(AppointmentService.class);

    @Transactional(readOnly = true)
    @Override
    public List<Appointment> getAllAppointmentByUsername(String username) {
        // 根据用户wxid查询所有记录并根据发起时间排序
        return appointmentRepository.findAllByLauncherEqualsOrderByLaunchDate(username);
    }

    @Transactional(readOnly = true)
    @Override
    public Appointment getAppointmentById(Integer id) {
        Appointment appointment = appointmentRepository.findAppointmentById(id);
        System.out.println(appointment);
        return appointment;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @Override
    public void addANewAppointment(Appointment appointment) {
        // 基于起始时间段和结束时间段

        String launcher = appointment.getLauncher();
//            getAppointment = appointmentRepository.findAppointmentByLauncherEqualsAndRoomIdEqualsAndExecDateEqualsAndLaunchTimeEqualsAndStatusEquals(launcher, roomId, execDate, launchTime, AppointmentStatus.NEW.getStatus());
        // 用户有新发起待批准、已批准通过、已签到的预约时不可再预约
        Appointment myAppointment = appointmentRepository.findAppointmentByLauncherWithStatus(launcher, AppointmentStatus.NEW.getStatus(), AppointmentStatus.RECEIVE.getStatus(), AppointmentStatus.SIGNED.getStatus());
        if (myAppointment != null) {
            throw new AppointmentDaoException("你有正在执行的预约，请等待审批！");
        }

        // 判断该房间所预约时间段是否空闲，比较房间ID，预约的日期，预约的时间段，不能对含有预约记录状态为receive、Signed
        Integer roomId = appointment.getRoomId();
//        Integer launchTime = appointment.getLaunchTime();
        Integer appointmentBegin = appointment.getBegin();
        Integer appointmentEnd = appointment.getEnd();

        Date execDate = appointment.getExecDate();
//        Appointment getAppointment = appointmentRepository.findReceivedAppointment(roomId, execDate, launchTime, AppointmentStatus.RECEIVE.getStatus(), AppointmentStatus.SIGNED.getStatus());
        Appointment getAppointment = appointmentRepository.findReceivedAppointment(roomId, execDate, appointmentBegin, appointmentEnd, AppointmentStatus.RECEIVE.getStatus(), AppointmentStatus.SIGNED.getStatus());

        if (getAppointment != null) {
            throw new AppointmentDaoException("该房间此时间段已被占用!");
        } else {
            appointment.setStatus(AppointmentStatus.NEW.getStatus());
            Date launchDate = new Date();
            // 还应该保证预约发起时间早于要预约的时间段的起始
            Schedule schedule = scheduleRepository.getOne(appointmentBegin);
            String begin = schedule.getBegin();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(execDate);
            String dateTime = date + " " + begin;
            Date executeDate = null;
            try {
                executeDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateTime);
            } catch (ParseException e) {
                throw new AppointmentDaoException("实际执行预约使用的时间格式有误！");
            }
            if (launchDate.before(executeDate)) {
                appointment.setLaunchDate(launchDate);
                appointmentRepository.save(appointment);
            } else {
                throw new AppointmentDaoException("该时间段已过，不可预约！");
            }
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @Override
    public List<Appointment> addNewAppointmentsByAdmin(List<Appointment> appointments) {
        List<String> adminLaunchers = adminRepository.findAll()
                .stream()
                .map(admin -> admin.getUsername())
                .collect(Collectors.toList());

        for (Appointment appointment : appointments) {
            // 在管理员预约的格式中，发起者为空
            if (appointment.getLauncher() != null) {
                throw new AppointmentDaoException("管理员预约的格式有误，发起者不为空!");
            }

            // 在管理员预约的格式中，审核者为管理员
            if (!adminLaunchers.contains(appointment.getConductor())) {
                throw new AppointmentDaoException("管理员预约的格式有误，审核者不为管理员!");
            }

            // 查找发生冲突的管理员预约管理员
            Integer roomId = appointment.getRoomId();
            Integer appointmentBegin = appointment.getBegin();
            Integer appointmentEnd = appointment.getEnd();
            Date execDate = appointment.getExecDate();
            List<Appointment> conflictingAppointments = appointmentRepository.findConflictingAppointmentsAppointedByAdmin(roomId, execDate, appointmentBegin, appointmentEnd, AppointmentStatus.RECEIVE.getStatus(), AppointmentStatus.SIGNED.getStatus());
            if (conflictingAppointments != null && conflictingAppointments.size() > 0) {
                throw new AppointmentDaoException("该房间此时间段已被其他管理员的预约占用！");
            } else {
                // 预约时间检查
                // 保证预约发起时间早于要预约的时间段的起始
                Date launchDate = new Date();
                Schedule schedule = scheduleRepository.getOne(appointmentBegin);
                String begin = schedule.getBegin();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = dateFormat.format(execDate);
                String dateTime = date + " " + begin;
                Date executeDate = null;
                try {
                    executeDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateTime);
                } catch (ParseException e) {
                    throw new AppointmentDaoException("实际执行预约使用的时间格式有误！");
                }
                if (launchDate.after(executeDate)) {
                    throw new AppointmentDaoException("该时间段已过，不可预约！");
                } else {
                    appointment.setLaunchDate(launchDate);
                }
            }
        }

        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : appointments) {
            appointment.setStatus(AppointmentStatus.RECEIVE.getStatus());
            Integer roomId = appointment.getRoomId();
            Integer appointmentBegin = appointment.getBegin();
            Integer appointmentEnd = appointment.getEnd();
            Date execDate = appointment.getExecDate();
            List<Appointment> conflictingAppointments = appointmentRepository.findConflictingAppointmentsAppointedByUser(roomId, execDate, appointmentBegin, appointmentEnd, AppointmentStatus.NEW.getStatus(), AppointmentStatus.RECEIVE.getStatus(), AppointmentStatus.SIGNED.getStatus());
            if (conflictingAppointments != null && conflictingAppointments.size() > 0) {
                conflictingAppointments.stream()
                        .forEach(a -> a.setStatus(AppointmentStatus.REJECT.getStatus()));
                result.addAll(conflictingAppointments);
            }
        }

        // 撤销冲突的用户预约
        appointmentRepository.saveAll(result);

        // 加入管理员预约
        appointmentRepository.saveAll(appointments);
        return result;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @Override
    public void cancelAppointmentsByAdminThroughIds(Integer[] ids) {
        List<Appointment> appointments = appointmentRepository.findAppointmentByIds(Arrays.asList(ids));
        if (appointments != null && appointments.size() == ids.length) {
            for (Appointment appointment : appointments) {
                if (appointment.getStatus().equals(AppointmentStatus.CANCEL.getStatus())) {
                    throw new AppointmentDaoException("该预约已被撤销，不可重复撤销!");
                }

                if (appointment.getStatus().equals(AppointmentStatus.ILLEGAL.getStatus()) || appointment.getStatus().equals(AppointmentStatus.FINISH.getStatus()) || appointment.getStatus().equals(AppointmentStatus.MISSED.getStatus())) {
                    throw new AppointmentDaoException("该预约已不可撤销!");
                }

                appointment.setStatus(AppointmentStatus.CANCEL.getStatus());
            }
        } else {
            throw new AppointmentDaoException("预约记录不存在");
        }

        appointmentRepository.saveAll(appointments);
    }


    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @Override
    public void cancelAppointmentById(Integer id) {
        Appointment appointment = appointmentRepository.findAppointmentById(id);
        if (appointment != null) {
//            if (appointment.getStatus().equals("receive")) {
//                throw new AppointmentDaoException("该预约已审批通过，不可撤销!");
//            }
            if (appointment.getStatus().equals("reject")) {
                throw new AppointmentDaoException("该预约已被审批驳回，不可撤销!");
            }
            if (appointment.getStatus().equals("cancel")) {
                throw new AppointmentDaoException("该预约已被撤销，不可重复撤销!");
            }
            if (appointment.getStatus().equals(AppointmentStatus.SIGNED.getStatus())) {
                throw new AppointmentDaoException("该预约已被签到，不可重复撤销!");
            }
            if (appointment.getStatus().equals(AppointmentStatus.ILLEGAL.getStatus()) || appointment.getStatus().equals(AppointmentStatus.FINISH.getStatus()) || appointment.getStatus().equals(AppointmentStatus.MISSED.getStatus())) {
                throw new AppointmentDaoException("该预约已不可撤销!");
            }
            // 撤销预约
            appointment.setStatus("cancel");
            appointmentRepository.save(appointment);
        } else {
            throw new AppointmentDaoException("不存在该预约记录!");
        }
    }

    @Override
    public List<Appointment> getAllAppointmentsAppointedByAdmin() {
        List<Appointment> appointments = appointmentRepository.findAppointmentAppointedByAdmin();
        return appointments;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Appointment> getAllAppointment(String status) {
        if (status != null && !status.equals("")) {
            return appointmentRepository.findAllByStatusEqualsOrderByExecDateAscLaunchTimeAscLaunchDateAsc(status);
        } else {
            return appointmentRepository.findAll();
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    @Override
    public void checkOutAppointment(Integer id, String status, String conductor, String checkNote) {
        Appointment appointment = appointmentRepository.findAppointmentById(id);
        if (appointment != null) {
            if (appointment.getStatus().equals(AppointmentStatus.CANCEL.getStatus())) {
                throw new AppointmentDaoException("该预约已经取消，无法审批!");
            }
            if (appointment.getStatus().equals(AppointmentStatus.RECEIVE.getStatus())) {
                if (!AppointmentStatus.SIGNED.getStatus().equals(status) && !AppointmentStatus.MISSED.getStatus().equals(status)) {
                    throw new AppointmentDaoException("该预约已审批通过，不可执行该操作!");
                }
            }
            if (appointment.getStatus().equals(AppointmentStatus.REJECT.getStatus())) {
                throw new AppointmentDaoException("该预约已审批驳回，不可重复操作!");
            }
            if (appointment.getStatus().equals(AppointmentStatus.MISSED.getStatus())) {
                if (!AppointmentStatus.SIGNED.getStatus().equals(status)) {
                    throw new AppointmentDaoException("该预约已爽约，只能重新签到!");
                }
            }
            if (appointment.getStatus().equals(AppointmentStatus.ILLEGAL.getStatus())) {
                if (!AppointmentStatus.FINISH.getStatus().equals(status)) {
                    throw new AppointmentDaoException("该预约未签退，只能修改为签退！");
                }
            }
            if (conductor != null && !conductor.equals("")) {
                if (appointment.getStatus().equals(AppointmentStatus.NEW.getStatus()) && AppointmentStatus.RECEIVE.getStatus().equals(status)) {
                    // 审批通过某一个预约，取消对该房间该时间段的其他预约，查询条件：roomId,时间段范围，状态：new
                    List<Appointment> list = appointmentRepository.getAppointmentsByRoomIdAndTimesAndStatus(appointment.getRoomId(), appointment.getBegin(), appointment.getEnd(), AppointmentStatus.NEW.getStatus());
                    for (Appointment app :
                            list) {
                        if (!app.getId().equals(appointment.getId())) {
                            app.setStatus(AppointmentStatus.REJECT.getStatus());
                            appointmentRepository.save(app);
                            notifyUser(app);
                        }
                    }
                }
                appointment.setStatus(status);
                appointment.setConductor(conductor);
                appointment.setCheckNote(checkNote);
                appointmentRepository.save(appointment);
                notifyUser(appointment);
            } else {
                throw new AppointmentDaoException("审批人姓名不可为空！");
            }
        } else {
            throw new AppointmentDaoException("预约不存在！");
        }
    }

    @Override
    public Page<Appointment> getAppointmentPages(Pageable pageable, String status) {
        if (status != null && !"".equals(status)) {
//            Appointment appointment = new Appointment();
//            appointment.setStatus(status);
//            Example<Appointment> example = Example.of(appointment);
            return appointmentRepository.findAllPagesByStatus(status, pageable);
        } else {
            return appointmentRepository.findAllPages(AppointmentStatus.CANCEL.getStatus(), AppointmentStatus.REJECT.getStatus() , pageable);
        }
    }

    @Override
    public Page<Appointment> getAppointmentsBySchoolId(Pageable pageable, String schoolId) {
        User user = userRepository.findUserBySchoolId(schoolId);
        if (user!=null) {
//            Appointment appointment = new Appointment();
//            appointment.setLauncher(user.getUsername());
//            Example<Appointment> example = Example.of(appointment);
            return appointmentRepository.findAllPagesByUsername(user.getUsername(), pageable);
        }
        throw new AppointmentDaoException("不存在该学号用户！");
    }

    private Date getExecDate(Date execDate,String begin) throws AppointmentDaoException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(execDate);
        String dateTime = date + " " + begin;
        try {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateTime);
        } catch (ParseException e) {
            throw new AppointmentDaoException("实际执行预约使用的时间格式有误！");
        }
    }

    private void notifyUser(Appointment appointment) {
            // 生成模板消息，通知预约人
            String launcherName = appointment.getLauncher();
            if(launcherName.length() > 0){
                WxMsgTemplateVo message = new WxMsgTemplateVo();
                User launcher = userRepository.findUserByUsername(launcherName);
                message.setName5(launcher.getName());
                Room room = roomsRepository.getOne(appointment.getRoomId());
                message.setThing2(room.getName());

                Date execDate = appointment.getExecDate();
                Schedule beginS = scheduleRepository.getOne(appointment.getBegin());
                String begin = beginS.getBegin();
                message.setDate3(getExecDate(execDate, begin));

                Schedule endS = scheduleRepository.getOne(appointment.getEnd());
                String end = endS.getEnd();
                message.setDate4(getExecDate(execDate, end));

                //TODO 状态的本地化显示
                if(appointment.getStatus().equals("receive")){
                    message.setPhrase8("通过");
                }else if(appointment.getStatus().equals("reject")){
                    message.setPhrase8("拒绝");
                }else{
                    message.setPhrase8("未知");
                }

                try{ //Notify fail should not cause appoint fail,so log and ignore failure
                    wechatService.notifyWechatUser(launcherName,message);
                }catch(Exception e){
                    logger.error("通知预约人时发生错误!");
                    logger.error(e);
                }
            }
    }


}
