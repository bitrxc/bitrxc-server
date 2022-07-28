package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.community.domain.Admin;
import cn.edu.bit.ruixin.community.domain.Appointment;
import cn.edu.bit.ruixin.community.domain.Room;
import cn.edu.bit.ruixin.community.domain.Schedule;
import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.domain.WxAppointmentChangeTemplateVo;
import cn.edu.bit.ruixin.community.domain.WxAppointmentPassTemplateVo;
import cn.edu.bit.ruixin.community.exception.AppointmentDaoException;
import cn.edu.bit.ruixin.community.myenum.AppointmentStatus;
import cn.edu.bit.ruixin.community.repository.AdminRepository;
import cn.edu.bit.ruixin.community.repository.AppointmentRepository;
import cn.edu.bit.ruixin.community.repository.RoomsRepository;
import cn.edu.bit.ruixin.community.repository.ScheduleRepository;
import cn.edu.bit.ruixin.community.repository.UserRepository;
import cn.edu.bit.ruixin.community.service.AppointmentService;
import cn.edu.bit.ruixin.community.service.WechatService;
import cn.edu.bit.ruixin.community.service.RoomService;

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
import java.util.*;
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

    @Autowired
    private RoomService roomService;

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

        // 获取用户预约请求中包含的字段
        String launcher = appointment.getLauncher();
        Integer roomId = appointment.getRoomId();
        Date execDate = appointment.getExecDate();
        Integer appointmentBegin = appointment.getBegin();
        Integer appointmentEnd = appointment.getEnd();

        // 用户有新发起待批准、已批准通过、已签到的预约(任何房间任何时间段)时不可再预约
        Appointment myAppointment = appointmentRepository.findAppointmentByLauncherWithStatus(launcher, AppointmentStatus.NEW.getStatus(), AppointmentStatus.RECEIVE.getStatus(), AppointmentStatus.SIGNED.getStatus());
        if (myAppointment != null) {
            throw new AppointmentDaoException("你有正在执行的预约，请等待审批！");
        }

        // 获取各时间段状态
        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(execDate);
        Map<String, List<Schedule>>scheduleListMap = roomService.getRoomFreeTime(roomId, launcher, dateStr);

        // 检查用户新发起的预约是否与已有预约冲突
        List<Schedule>busyTime = scheduleListMap.get("busyTime");
        Set<Integer>busyTimeId = new HashSet<>();
        busyTime.forEach(schedule -> busyTimeId.add(schedule.getId()));
        for(int scheduleNum = appointmentBegin; scheduleNum <= appointmentEnd; scheduleNum++) {
            if(busyTimeId.contains(scheduleNum)) {
                throw new AppointmentDaoException("该房间此时间段已被占用!");
            }
        }

        // 检查用户的预约时段是否已过
        List<Schedule>passTime = scheduleListMap.get("passTime");
        Set<Integer>passTimeId = new HashSet<>();
        passTime.forEach(schedule -> passTimeId.add(schedule.getId()));
        if(passTimeId.contains(appointmentBegin))  {
            throw new AppointmentDaoException("该时间段已过，不可预约！");
        }

        // 检查通过则执行预约
        appointment.setLaunchDate(new Date());
        appointment.setStatus(AppointmentStatus.NEW.getStatus());
        appointmentRepository.save(appointment);
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

            // 获取管理员预约请求中包含的字段
            Integer roomId = appointment.getRoomId();
            String conductor = appointment.getConductor();
            Integer appointmentBegin = appointment.getBegin();
            Integer appointmentEnd = appointment.getEnd();
            Date execDate = appointment.getExecDate();

            // 获取各时间段状态
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(execDate);
            Map<String, List<Schedule>> scheduleListMap = roomService.getRoomFreeTimeByAdmin(roomId, conductor, dateStr);

            Map<Integer, Boolean> scheduleIdMap = new HashMap<>(); // <scheduleId, 是否被占用>
            // 占用状态包括所有管理员(包括当前管理员)的预约
            scheduleListMap.get("busyTime").forEach(schedule -> scheduleIdMap.put(schedule.getId(), true));
            scheduleListMap.get("myTime").forEach(schedule -> scheduleIdMap.put(schedule.getId(), true));
            scheduleListMap.get("passTime").forEach(schedule -> scheduleIdMap.put(schedule.getId(), false));

            boolean scheduleOccupied = false;
            boolean scheduleAvailable = true;
            for(int scheduleIdNum = appointmentBegin; scheduleIdNum <= appointmentEnd; scheduleIdNum++) {
                if(scheduleIdMap.get(scheduleIdNum) != null) {
                    scheduleOccupied = scheduleIdMap.get(scheduleIdNum);
                    scheduleAvailable = scheduleIdMap.get(scheduleIdNum);
                    break;
                }
            }

            if(scheduleOccupied)  {
                throw new AppointmentDaoException("预约时间段已被管理员占用！");
            }

            if(!scheduleAvailable) {
                throw new AppointmentDaoException("已过时间段不可预约！");
            }
        }

        List<Appointment> result = new ArrayList<>();
        // 执行管理员预约
        for (Appointment appointment : appointments) {

            appointment.setLaunchDate(new Date());
            appointment.setStatus(AppointmentStatus.RECEIVE.getStatus());

            List<Appointment> conflictingAppointments = appointmentRepository.findConflictingAppointmentsAppointedByUser(appointment.getRoomId(), appointment.getExecDate(), appointment.getBegin(), appointment.getEnd(), AppointmentStatus.NEW.getStatus(), AppointmentStatus.RECEIVE.getStatus(), AppointmentStatus.SIGNED.getStatus());
            if (conflictingAppointments != null && conflictingAppointments.size() > 0) {
                //管理员预约覆盖冲突的用户预约
                conflictingAppointments.stream().forEach(a -> {
                    a.setStatus(AppointmentStatus.REJECT.getStatus());
                    notifyUserChange(a);
                });
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

    @Transactional(readOnly = true)
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
        if (conductor == null || conductor.equals("")) {
            throw new AppointmentDaoException("审批人姓名不可为空！");
        }
        if (checkNote == null) {
            checkNote = "";
            // throw new AppointmentDaoException("审批意见不可为空！");
        }
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
            if (appointment.getStatus().equals(AppointmentStatus.NEW.getStatus()) && AppointmentStatus.RECEIVE.getStatus().equals(status)) {
                // 审批通过某一个预约，取消对该房间该时间段的其他预约，查询条件：roomId,时间段范围，状态：new
                List<Appointment> list = appointmentRepository.getAppointmentsByRoomIdAndTimesAndStatus(appointment.getRoomId(), appointment.getBegin(), appointment.getEnd(), AppointmentStatus.NEW.getStatus());
                for (Appointment app :
                        list) {
                    if (!app.getId().equals(appointment.getId())) {
                        app.setStatus(AppointmentStatus.REJECT.getStatus());
                        appointmentRepository.save(app);
                        notifyUserSuccess(app);
                    }
                }
            }
            appointment.setStatus(status);
            appointment.setConductor(conductor);
            appointment.setCheckNote(checkNote);
            appointmentRepository.save(appointment);
            notifyUserSuccess(appointment);
        } else {
            throw new AppointmentDaoException("预约不存在！");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Appointment> getAppointmentPages(Pageable pageable, String status) {
        if (status != null && !"".equals(status)) {
            return appointmentRepository.findAllPagesByStatus(status, pageable);
        } else {
            return appointmentRepository.findAllPages(AppointmentStatus.CANCEL.getStatus(), AppointmentStatus.REJECT.getStatus() , pageable);
        }
    }

    @Transactional(readOnly = true)
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

    /**
     * 根据预约执行的日期和当天的时间来生成预约执行的时间
     * @param execDate
     * @param timeStr 预约的时间，应为某一个时间段的begin或end属性
     * @return
     * @throws AppointmentDaoException
     */
    private Date getExecDate(Date execDate,String timeStr) throws AppointmentDaoException{
        String execDateStr = new SimpleDateFormat("yyyy-MM-dd").format(execDate);
        String dateTimeStr = execDateStr + " " + timeStr;
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTimeStr);
        } catch (ParseException e) {
            throw new AppointmentDaoException("实际执行预约使用的时间格式有误！");
        }
    }

    private void notifyUserSuccess(Appointment appointment) {
        // 生成模板消息，通知预约人
        String launcherName = appointment.getLauncher();
        if(launcherName != null &&  !launcherName.equals("")){
            WxAppointmentPassTemplateVo message = new WxAppointmentPassTemplateVo();
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

    
    private void notifyUserChange(Appointment appointment) {
        // 生成模板消息，通知预约人
        String launcherName = appointment.getLauncher();
        if(launcherName != null &&  !launcherName.equals("")){
            WxAppointmentChangeTemplateVo message = new WxAppointmentChangeTemplateVo();
            User launcher = userRepository.findUserByUsername(launcherName);
            message.setThing9(launcher.getName());

            message.setThing17(appointment.getCheckNote());

            message.setDate2(appointment.getLaunchDate());

            //TODO 状态的本地化显示
            if(appointment.getStatus().equals("reject")){
                message.setPhrase16("预约失效");
            }else{
                message.setPhrase16("未知");
            }

            try{ //Notify fail should not cause appoint fail,so log and ignore failure
                wechatService.notifyWechatUser(launcherName,message);
            }catch(Exception e){
                logger.error("通知被拒绝预约人时发生错误!");
                logger.error(e);
            }
        }
    }

}
