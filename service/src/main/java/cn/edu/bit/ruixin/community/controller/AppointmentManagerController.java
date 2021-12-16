package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
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
import lombok.Getter;

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
    public CommonResult lookupAppointmentById(@RequestParam("id") Integer id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        AppointmentInfoVo infoVo = getAppointmentInfoVo(appointment);

        return CommonResult.ok(ResultCode.SUCCESS).data("appointment", infoVo);
    }

    @PreAuthorize("hasAuthority('appointCheck')")
    @GetMapping("/{current}/{limit}/{schoolId}")
    public CommonResult lookupAppointmentBySchoolId(@PathVariable("current") int current, @PathVariable("limit") int limit, @PathVariable("schoolId") String schoolId) {
        // 构造分页对象
        Pageable pageable = PageRequest.of(current, limit);
        Page<Appointment> page = appointmentService.getAppointmentsBySchoolId(pageable, schoolId);
        List<Appointment> list = page.getContent();

        List<AppointmentInfoVo> infoVos = new ArrayList<>();
        list.forEach(appointment -> infoVos.add(getAppointmentInfoVo(appointment)));

        Map<String, Object> map = new HashMap<>();
        map.put("totalElements", page.getTotalElements());
        map.put("totalPages", page.getTotalPages());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        map.put("items", infoVos);
        return CommonResult.ok(ResultCode.SUCCESS).data(map);
    }

    /**
     * 以不分页的方式，查询所有预约。可以查询某一状态的预约
     * @param status
     * @return
     */
    @PreAuthorize("hasAuthority('appointCheck')")
    @GetMapping("all")
    public CommonResult getAllAppointment(@RequestParam(required = false, name = "status") String status) {
        List<Appointment> list = appointmentService.getAllAppointment(status);
        List<AppointmentInfoVo> infoVos = new ArrayList<>();
        list.forEach(appointment -> infoVos.add(getAppointmentInfoVo(appointment)));

        return CommonResult.ok(ResultCode.SUCCESS).data("appointments", infoVos);
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
    public CommonResult getAppointmentPages(@PathVariable("current") int current, @PathVariable("limit") int limit, @RequestParam(required = false, name = "status") String status) {
        // 构造排序对象
//        Sort sort = Sort.by(Sort.Direction.DESC, "launchDate", "execDate", "launchTime");
        // 构造分页对象
        Pageable pageable = PageRequest.of(current, limit);
        Page<Appointment> page = appointmentService.getAppointmentPages(pageable, status);
        List<Appointment> list = page.getContent();

        List<AppointmentInfoVo> infoVos = new ArrayList<>();
        list.forEach(appointment -> infoVos.add(getAppointmentInfoVo(appointment)));

        Map<String, Object> map = new HashMap<>();
        map.put("totalElements", page.getTotalElements());
        map.put("totalPages", page.getTotalPages());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        map.put("items", infoVos);
        return CommonResult.ok(ResultCode.SUCCESS).data(map);
    }

    @PreAuthorize("hasAuthority('appointCheck')")
    @MsgSecCheck({"conductor", "checkNote"})
    @PutMapping("/check/{appointmentId}")
    public CommonResult check(@PathVariable(name = "appointmentId") Integer id,
                              @RequestParam(required = true, name = "status") String status,
                              @RequestParam(required = true, name = "conductor") String conductor,
                              @RequestParam("checkNote") String checkNote) {
        appointmentService.checkOutAppointment(id, status, conductor, checkNote);
        return CommonResult.ok(ResultCode.SUCCESS).msg("审批操作成功!");
    }

    /**
     * 发起管理员预约。与一般用户预约不同，管理员预约的发起者字段为空。
     * @param infoVos
     * @return
     */
    @PreAuthorize("hasAuthority('appoint')")
    @PostMapping("/appoint")
    public CommonResult appoint(@RequestBody(required = true) AppointmentInfoVo[] infoVos) {
        List<Appointment> appointments = Arrays.stream(infoVos)
                .map(infoVo -> AppointmentInfoVo.convertToPo(infoVo))
                .collect(Collectors.toList());

        List<Appointment> deletedAppointments = appointmentService.addNewAppointmentsByAdmin(appointments);

        return CommonResult.ok(ResultCode.SUCCESS).msg("管理员预约成功").data("conflictingAppointments",
                deletedAppointments.stream()
                        .map(appointment -> AppointmentInfoVo.convertToVo(appointment))
                        .collect(Collectors.toList()));
    }

    /**
     * 撤销管理员预约
     * @param ids
     * @return
     */
    @PreAuthorize("hasAuthority('appoint')")
    @PutMapping("/cancel")
    public CommonResult cancel(@RequestBody(required = true) Integer[] ids) {
        appointmentService.cancelAppointmentsByAdminThroughIds(ids);
        return CommonResult.ok(ResultCode.SUCCESS).msg("撤销预约成功!");
    }

    /**
     * 查阅所有管理员预约
     * @return
     */
    @PreAuthorize("hasAuthority('appoint')")
    @GetMapping("/allByAdmin")
    public CommonResult getAllAppointmentAppointedByAdmin() {
        List<Appointment> appointments = appointmentService.getAllAppointmentsAppointedByAdmin();
        return CommonResult.ok(ResultCode.SUCCESS)
                .data("appointments", appointments.stream()
                        .map(appointment -> {
                            AppointmentInfoVo appointmentInfoVo = AppointmentInfoVo.convertToVo(appointment);
                            Room room = roomService.getRoomInfoById(appointment.getRoomId());
                            appointmentInfoVo.setRoomName(room.getName());
                            return appointmentInfoVo;
                        })
                        .collect(Collectors.toList()));
    }

    /**
     * 管理员预约的可用房间时段
     * @return
     */
    @PreAuthorize("hasAuthority('appoint')")
    @GetMapping("/availablePeriod")
    public CommonResult getAvailablePeriodByRoomId(@RequestParam(name = "roomId") Integer roomId, @RequestParam(name = "conductor") String conductor, @RequestParam(name = "date") String date) {
        Map result = roomService.getRoomFreeTimeByAdmin(roomId, conductor, date);
        return CommonResult.ok(ResultCode.SUCCESS)
                .data(result);
    }

    /**
     * 所有时间段
     * @return
     */
    @PreAuthorize("hasAuthority('appointCheck')")
    @GetMapping("/allTime")
    public CommonResult getAllTimeByAdmin() {
        List<Schedule> scheduleList = scheduleService.getAllTime();

        @Getter
        class scheduleBeginTime {
            private Integer id;
            private String beginTime;

            public scheduleBeginTime(Integer id, String beginTime) {
                this.id = id;
                this.beginTime = beginTime;
            }
        }

        @Getter
        class scheduleEndTime {
            private Integer id;
            private String endTime;

            public scheduleEndTime(Integer id, String endTime) {
                this.id = id;
                this.endTime = endTime;
            }
        }

        return CommonResult.ok(ResultCode.SUCCESS)
                .data("beginTimes", scheduleList.stream()
                        .map(schedule -> new scheduleBeginTime(schedule.getId(), schedule.getBegin()))
                        .collect(Collectors.toList()))
                .data("endTimes", scheduleList.stream()
                        .map(schedule -> new scheduleEndTime(schedule.getId(), schedule.getEnd()))
                        .collect(Collectors.toList()));
    }
}
