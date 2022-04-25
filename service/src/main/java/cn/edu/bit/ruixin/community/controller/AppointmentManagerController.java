package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.exp.CommonResult;
import cn.edu.bit.ruixin.community.annotation.MsgSecCheck;
import cn.edu.bit.ruixin.community.domain.Appointment;
import cn.edu.bit.ruixin.community.domain.Room;
import cn.edu.bit.ruixin.community.domain.Schedule;
import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.service.AppointmentService;
import cn.edu.bit.ruixin.community.service.RoomService;
import cn.edu.bit.ruixin.community.service.ScheduleService;
import cn.edu.bit.ruixin.community.service.UserService;
import cn.edu.bit.ruixin.community.vo.AppointmentInfoVo;
import cn.edu.bit.ruixin.community.vo.PageVo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/6
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/appointment")
public class AppointmentManagerController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ScheduleService scheduleService;

    private AppointmentInfoVo getAppointmentInfoVo(Appointment appointment) {
        AppointmentInfoVo infoVo = AppointmentInfoVo.convertToVo(appointment);
        if (infoVo.getLauncher() == null) {
            infoVo.setUsername(infoVo.getConductor());
        } else {
            User user = userService.getUserByUsername(infoVo.getLauncher());
            infoVo.setUsername(user.getName());
            infoVo.setSchoolId(user.getSchoolId());
        }
        Room room = roomService.getRoomInfoById(infoVo.getRoomId());
        infoVo.setRoomName(room.getName());
        return infoVo;
    }

    @PreAuthorize("hasAuthority('appointCheck')")
    @GetMapping("")
    public CommonResult<AppointmentInfoReturnVo> lookupAppointmentById(@RequestParam("id") Integer id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        AppointmentInfoVo infoVo = getAppointmentInfoVo(appointment);

        return CommonResult.<AppointmentInfoReturnVo>ok()
            .data(new AppointmentInfoReturnVo(infoVo));
    }

    @PreAuthorize("hasAuthority('appointCheck')")
    @GetMapping("/{current}/{limit}/{schoolId}")
    public CommonResult<PageVo<AppointmentInfoVo>> lookupAppointmentBySchoolId(@PathVariable("current") int current, @PathVariable("limit") int limit, @PathVariable("schoolId") String schoolId) {
        // 构造分页对象
        Pageable pageable = PageRequest.of(current, limit);
        Page<Appointment> page = appointmentService.getAppointmentsBySchoolId(pageable, schoolId);

        PageVo<AppointmentInfoVo> res = PageVo.convertToVo(page)
            .foreach(appointment -> getAppointmentInfoVo(appointment));
        return CommonResult.<PageVo<AppointmentInfoVo>>ok()
            .data(res);
    }

    /**
     * 以不分页的方式，查询所有预约。可以查询某一状态的预约
     * @param status
     * @return
     */
    @PreAuthorize("hasAuthority('appointCheck')")
    @GetMapping("all")
    public CommonResult<AppointmentListReturnVo> getAllAppointment(@RequestParam(required = false, name = "status") String status) {
        List<Appointment> list = appointmentService.getAllAppointment(status);
        List<AppointmentInfoVo> infoVos = new ArrayList<>();
        list.forEach(appointment -> infoVos.add(getAppointmentInfoVo(appointment)));

        return CommonResult.<AppointmentListReturnVo>ok()
            .data(new AppointmentListReturnVo(infoVos));
    }

    /**
     * 以*分页*的方式，查询所有预约。可以查询某一状态的预约
     *
     * @param current
     * @param limit
     * @return
     */
    @PreAuthorize("hasAuthority('appointCheck')")
    @GetMapping("/{current}/{limit}")
    public CommonResult<PageVo<AppointmentInfoVo>> getAppointmentPages(@PathVariable("current") int current, @PathVariable("limit") int limit, @RequestParam(required = false, name = "status") String status) {
        // 构造排序对象
//        Sort sort = Sort.by(Sort.Direction.DESC, "launchDate", "execDate", "launchTime");
        // 构造分页对象
        Pageable pageable = PageRequest.of(current, limit);
        Page<Appointment> page = appointmentService.getAppointmentPages(pageable, status);
        PageVo<AppointmentInfoVo> res = PageVo.convertToVo(page)
            .foreach(appointment -> getAppointmentInfoVo(appointment));

        return CommonResult.<PageVo<AppointmentInfoVo>>ok().data(res);
    }

    @PreAuthorize("hasAuthority('appointCheck')")
    @MsgSecCheck({"conductor", "checkNote"})
    @PutMapping("/check/{appointmentId}")
    public CommonResult<Void> check(@PathVariable(name = "appointmentId") Integer id,
                              @RequestParam(required = true, name = "status") String status,
                              @RequestParam(required = true, name = "conductor") String conductor,
                              @RequestParam("checkNote") String checkNote) {
        appointmentService.checkOutAppointment(id, status, conductor, checkNote);
        return CommonResult.<Void>ok().msg("审批操作成功!");
    }

    /**
     * 发起管理员预约。与一般用户预约不同，管理员预约的发起者字段为空。
     * @param infoVos
     * @return 所有被撤销的管理员预约
     */
    @PreAuthorize("hasAuthority('appoint')")
    @PostMapping("/appoint")
    public CommonResult<AppointmentConfilctingReturnVo> appoint(@RequestBody(required = true) AppointmentInfoVo[] infoVos) {
        List<Appointment> appointments = Arrays.stream(infoVos)
                .map(infoVo -> AppointmentInfoVo.convertToPo(infoVo))
                .collect(Collectors.toList());

        List<Appointment> deletedAppointments = appointmentService.addNewAppointmentsByAdmin(appointments);
        AppointmentConfilctingReturnVo res = new AppointmentConfilctingReturnVo(
            deletedAppointments.stream()
                .map(appointment -> getAppointmentInfoVo(appointment))
                .collect(Collectors.toList()));

        return CommonResult.<AppointmentConfilctingReturnVo>ok()
            .msg("管理员预约成功").data(res);
    }

    /**
     * 撤销管理员预约
     * @param ids
     * @return
     */
    @PreAuthorize("hasAuthority('appoint')")
    @PutMapping("/cancel")
    public CommonResult<Void> cancel(@RequestBody(required = true) Integer[] ids) {
        appointmentService.cancelAppointmentsByAdminThroughIds(ids);
        return CommonResult.<Void>ok().msg("撤销预约成功!");
    }

    /**
     * 查阅所有管理员预约
     * @return
     */
    @PreAuthorize("hasAuthority('appoint')")
    @GetMapping("/allByAdmin")
    public CommonResult<AppointmentListReturnVo> getAllAppointmentAppointedByAdmin() {
        List<Appointment> appointments = appointmentService.getAllAppointmentsAppointedByAdmin();
        return CommonResult.<AppointmentListReturnVo>ok()
            .data(new AppointmentListReturnVo(
                appointments.stream()
                    .map(appointment -> {
                        AppointmentInfoVo appointmentInfoVo = getAppointmentInfoVo(appointment);
                        return appointmentInfoVo;
                    })
                    .collect(Collectors.toList())));
    }

    /**
     * 管理员预约的可用房间时段
     * @return
     */
    @PreAuthorize("hasAuthority('appoint')")
    @GetMapping("/availablePeriod")
    public CommonResult<Map<String,List<Schedule>>> getAvailablePeriodByRoomId(@RequestParam(name = "roomId") Integer roomId, @RequestParam(name = "conductor") String conductor, @RequestParam(name = "date") String date) {
        Map<String,List<Schedule>> result = roomService.getRoomFreeTimeByAdmin(roomId, conductor, date);
        return CommonResult.<Map<String,List<Schedule>>>ok()
                .data(result);
    }

    /**
     * 所有时间段
     * @return
     */
    @PreAuthorize("hasAuthority('appointCheck')")
    @GetMapping("/allTime")
    public CommonResult<ScheduleListVo> getAllTimeByAdmin() {
        List<Schedule> scheduleList = scheduleService.getAllTime();
        return CommonResult.<ScheduleListVo>ok()
            .data(ScheduleListVo.getFrom(scheduleList));
    }
}

@Getter
@NoArgsConstructor
class ScheduleListVo {
    
    @Getter
    class scheduleBeginTime {
        private Integer id;
        private String beginTime;

        public scheduleBeginTime(Schedule schedule) {
            this.id = schedule.getId();
            this.beginTime = schedule.getBegin();
        }
    }

    @Getter
    class scheduleEndTime {
        private Integer id;
        private String endTime;

        public scheduleEndTime(Schedule schedule) {
            this.id = schedule.getId();
            this.endTime = schedule.getEnd();
        }
    }
    private List<scheduleBeginTime> beginTimes;
    private List<scheduleEndTime> endTimes;

    public static ScheduleListVo getFrom(List<Schedule> scheduleList) {
        ScheduleListVo res = new ScheduleListVo();
        res.beginTimes = scheduleList.stream()
            .map(schedule -> res.new scheduleBeginTime(schedule))
            .collect(Collectors.toList());
        res.endTimes = scheduleList.stream()
            .map(schedule -> res.new scheduleEndTime(schedule))
            .collect(Collectors.toList());
        return res;
    }
}

@Getter
class AppointmentInfoReturnVo {
    private AppointmentInfoVo appointment;
    
    AppointmentInfoReturnVo(AppointmentInfoVo infovo){
        this.appointment = infovo;
    }
}

@Getter
class AppointmentListReturnVo {
    private List<AppointmentInfoVo> appointments;
    
    AppointmentListReturnVo(List<AppointmentInfoVo> infovo){
        this.appointments = infovo;
    }
}

@Getter
class AppointmentConfilctingReturnVo {
    private List<AppointmentInfoVo> conflictingAppointments;
    
    AppointmentConfilctingReturnVo(List<AppointmentInfoVo> infovo){
        this.conflictingAppointments = infovo;
    }
}
