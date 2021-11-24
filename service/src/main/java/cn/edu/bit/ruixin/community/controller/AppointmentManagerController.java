package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.community.annotation.MsgSecCheck;
import cn.edu.bit.ruixin.community.domain.Appointment;
import cn.edu.bit.ruixin.community.domain.Room;
import cn.edu.bit.ruixin.community.domain.Schedule;
import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.myenum.AppointmentStatus;
import cn.edu.bit.ruixin.community.service.AppointmentService;
import cn.edu.bit.ruixin.community.service.RoomService;
import cn.edu.bit.ruixin.community.service.ScheduleService;
import cn.edu.bit.ruixin.community.service.UserService;
import cn.edu.bit.ruixin.community.vo.AppointmentInfoVo;
import cn.edu.bit.ruixin.community.vo.RoomInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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


    @GetMapping("")
    public CommonResult lookupAppointmentById(@RequestParam("id") Integer id) {
        Appointment appointment = appointmentService.getAppointmentById(id);

        AppointmentInfoVo infoVo = AppointmentInfoVo.convertToVo(appointment);
        User user = userService.getUserByUsername(infoVo.getLauncher());
        Room room = roomService.getRoomInfoById(infoVo.getRoomId());
        infoVo.setUsername(user.getName());
        infoVo.setRoomName(room.getName());
        infoVo.setSchoolId(user.getSchoolId());

        return CommonResult.ok(ResultCode.SUCCESS).data("appointment", infoVo);
    }

    @GetMapping("/{current}/{limit}/{schoolId}")
    public CommonResult lookupAppointmentBySchoolId(@PathVariable("current") int current, @PathVariable("limit") int limit, @PathVariable("schoolId") String schoolId) {
        // 构造分页对象
        Pageable pageable = PageRequest.of(current, limit);
        Page<Appointment> page = appointmentService.getAppointmentsBySchoolId(pageable, schoolId);
        List<Appointment> list = page.getContent();

        List<AppointmentInfoVo> infoVos = new ArrayList<>();
        for (Appointment appointment :
                list) {
            AppointmentInfoVo infoVo = AppointmentInfoVo.convertToVo(appointment);
            User user = userService.getUserByUsername(infoVo.getLauncher());
            Room room = roomService.getRoomInfoById(infoVo.getRoomId());
            infoVo.setUsername(user.getName());
            infoVo.setRoomName(room.getName());
            infoVo.setSchoolId(user.getSchoolId());
            infoVos.add(infoVo);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("totalElements", page.getTotalElements());
        map.put("totalPages", page.getTotalPages());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        map.put("items", infoVos);
        return CommonResult.ok(ResultCode.SUCCESS).data(map);
    }

    @GetMapping("all")
    public CommonResult getAllAppointment(@RequestParam(required = false, name = "status") String status) {
        List<Appointment> list = appointmentService.getAllAppointment(status);
        List<AppointmentInfoVo> infoVos = new ArrayList<>();

        for (Appointment appointment : list
        ) {
            AppointmentInfoVo infoVo = AppointmentInfoVo.convertToVo(appointment);
            User user = userService.getUserByUsername(infoVo.getLauncher());
            Room room = roomService.getRoomInfoById(infoVo.getRoomId());
            infoVo.setSchoolId(user.getSchoolId());
            infoVo.setUsername(user.getName());
            infoVo.setRoomName(room.getName());
            infoVos.add(infoVo);
        }
        return CommonResult.ok(ResultCode.SUCCESS).data("appointments", infoVos);
    }

    /**
     * 分页查询
     *
     * @param current
     * @param limit
     * @return
     */

    /**
     * @api {Get} /admin/appointment/{current}/{limit} 获取预约记录列表
     * @apiGroup test3
     * @apiDescription 获取预约记录列表
     * @apiParam {Integer} current 当前页, 路径占位符。
     * @apiParam {Integer} limit 当前页记录数, 路径传值。
     * @apiParam {String} [userName] 预约记录状态。枚举值。可选参数，可不传。房间状态枚举如下:
     *
      "executing"：房间使用中
      "receive"： 通过
      "rejected"：拒绝
      "new": 提出申请待审批
     * @apiSuccess {Number} totalpage 总记录页
     * @apiSuccess {Boolean} hasPrevious 是否有前一页
     * @apiSuccess {Boolean} hasNext 是否有后一页
     * @apiSuccess {Number} totalElements 一页中的元素项数, 即 items 中有多少项
     * @apiSuccess {Array} items 预约记录集合。各项具体含义如下：
     *
      "id"： 预约记录 ID
      "begin": ?
      "end": ?
      "roomId": 房间 ID
      "launchTime": ? 预约时间吗？
      "launcher": ? 预约人吗？
      "status": 房间状态
      "conductor": ?
      "checkNote": ?
      "userNote": ?
      "checkDate": ?
      "launchDate": ?
      "execDate": ?
     * @apiSuccessExample {json} 响应数据示例
     * {
     *     "success": true,
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": {
     *         "totalPages": 1,
     *         "hasPrevious": false,
     *         "hasNext": false,
     *         "totalElements": 3,
     *         "items": [
     *             {
     *                 "id": 3,
     *                 "begin": null,
     *                 "end": null,
     *                 "roomId": 1,
     *                 "launchTime": 1,
     *                 "launcher": "123456",
     *                 "status": "receive",
     *                 "conductor": null,
     *                 "checkNote": null,
     *                 "userNote": null,
     *                 "checkDate": 0,
     *                 "launchDate": 0,
     *                 "execDate": null
     *             },
     *             {
     *                 "id": 18,
     *                 "begin": null,
     *                 "end": null,
     *                 "roomId": 1,
     *                 "launchTime": 3,
     *                 "launcher": "omn3g4s_YpmXsN8n38iPwIrR5Gxk",
     *                 "status": "receive",
     *                 "conductor": null,
     *                 "checkNote": null,
     *                 "userNote": null,
     *                 "checkDate": 0,
     *                 "launchDate": 1614155056000,
     *                 "execDate": "2021-02-26"
     *             },
     *             {
     *                 "id": 23,
     *                 "begin": null,
     *                 "end": null,
     *                 "roomId": 1,
     *                 "launchTime": 5,
     *                 "launcher": "omn3g4s_YpmXsN8n38iPwIrR5Gxk",
     *                 "status": "receive",
     *                 "conductor": null,
     *                 "checkNote": null,
     *                 "userNote": null,
     *                 "checkDate": 0,
     *                 "launchDate": 1614172187000,
     *                 "execDate": "2021-02-26"
     *             }
     *         ]
     *     }
     * }
     */
    @GetMapping("/{current}/{limit}")
    public CommonResult getAppointmentPages(@PathVariable("current") int current, @PathVariable("limit") int limit, @RequestParam(required = false, name = "status") String status) {
        // 构造排序对象
//        Sort sort = Sort.by(Sort.Direction.DESC, "launchDate", "execDate", "launchTime");
        // 构造分页对象
        Pageable pageable = PageRequest.of(current, limit);
        Page<Appointment> page = appointmentService.getAppointmentPages(pageable, status);
        List<Appointment> list = page.getContent();

        List<AppointmentInfoVo> infoVos = new ArrayList<>();
        for (Appointment appointment :
                list) {
            AppointmentInfoVo infoVo = AppointmentInfoVo.convertToVo(appointment);
            User user = userService.getUserByUsername(infoVo.getLauncher());
            Room room = roomService.getRoomInfoById(infoVo.getRoomId());
            infoVo.setSchoolId(user.getSchoolId());
            infoVo.setUsername(user.getName());
            infoVo.setRoomName(room.getName());
//            if (status == null || "".equals(status)) {
//                if (AppointmentStatus.CANCEL.getStatus().equals(infoVo.getStatus()) || AppointmentStatus.REJECT.getStatus().equals(infoVo.getStatus())) {
//                    continue;
//                }
//            }
            infoVos.add(infoVo);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("totalElements", page.getTotalElements());
        map.put("totalPages", page.getTotalPages());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        map.put("items", infoVos);
        return CommonResult.ok(ResultCode.SUCCESS).data(map);
    }

    /**
     * @api {Put} /admin/appointment/check/{appointmentId} 审批预约记录、预约签到、预约完成记录
     * @apiGroup 预约管理
     * @apiDescription 审批预约记录、预约签到、预约完成记录
     * @apiBody {AppointmentInfoVo[]} infoVos 请求体，携带批量预约的房间号信息
     * @apiSuccessExample {json} 响应数据示例
     * {
     *     "success": true,
     *     "code": 200,
     *     "message": "管理员预约操作成功",
     *     "data": {
     *         "conflictingAppointments": [
     *             {
     *                 "id": 3,
     *                 "begin": null,
     *                 "end": null,
     *                 "roomId": 1,
     *                 "launchTime": 1,
     *                 "launcher": "123456",
     *                 "status": "receive",
     *                 "conductor": null,
     *                 "checkNote": null,
     *                 "userNote": null,
     *                 "checkDate": 0,
     *                 "launchDate": 0,
     *                 "execDate": null
     *             },
     *             {
     *                 "id": 18,
     *                 "begin": null,
     *                 "end": null,
     *                 "roomId": 1,
     *                 "launchTime": 3,
     *                 "launcher": "omn3g4s_YpmXsN8n38iPwIrR5Gxk",
     *                 "status": "receive",
     *                 "conductor": null,
     *                 "checkNote": null,
     *                 "userNote": null,
     *                 "checkDate": 0,
     *                 "launchDate": 1614155056000,
     *                 "execDate": "2021-02-26"
     *             }
     *         ]
     *     }
     * }
     */
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
     * @api {Post} /admin/appointment/appoint 管理员批量预约接口
     * @apiGroup 管理员预约管理
     * @apiDescription 管理员批量预约接口
     * @apiBody {AppointmentInfoVo[]} infoVos 请求体，携带批量预约的房间号信息
     * @apiSuccessExample {json} 响应数据示例
     * {
     *     "success": true,
     *     "code": 200,
     *     "message": "管理员预约操作成功",
     *     "data": {
     *         "conflictingAppointments": [
     *             {
     *                 "id": 3,
     *                 "begin": null,
     *                 "end": null,
     *                 "roomId": 1,
     *                 "launchTime": 1,
     *                 "launcher": "123456",
     *                 "status": "receive",
     *                 "conductor": null,
     *                 "checkNote": null,
     *                 "userNote": null,
     *                 "checkDate": 0,
     *                 "launchDate": 0,
     *                 "execDate": null
     *             },
     *             {
     *                 "id": 18,
     *                 "begin": null,
     *                 "end": null,
     *                 "roomId": 1,
     *                 "launchTime": 3,
     *                 "launcher": "omn3g4s_YpmXsN8n38iPwIrR5Gxk",
     *                 "status": "receive",
     *                 "conductor": null,
     *                 "checkNote": null,
     *                 "userNote": null,
     *                 "checkDate": 0,
     *                 "launchDate": 1614155056000,
     *                 "execDate": "2021-02-26"
     *             }
     *         ]
     *     }
     * }
     */
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
     * @api {Put} /admin/appointment/cancel 管理员批量撤销预约接口
     * @apiGroup 管理员预约管理
     * @apiDescription 管理员批量撤销预约接口
     * @apiBody {Integer[]} ids 预约记录id集合
     */
    @PutMapping("/cancel")
    public CommonResult cancel(@RequestBody(required = true) Integer[] ids) {
        appointmentService.cancelAppointmentsByAdminThroughIds(ids);
        return CommonResult.ok(ResultCode.SUCCESS).msg("撤销预约成功!");
    }

    /**
     * @api {Get} /admin/appointment/allByAdmin 查看管理员所有预约记录
     * @apiGroup 管理员预约管理
     * @apiDescription 查看管理员所有预约记录
     * @apiSuccess {Array} appointments 各项具体含义如下：
     *
      "id"： 预约记录 ID
      "begin": ?
      "end": ?
      "roomId": 房间 ID
      "launchTime": ? 预约时间吗？
      "launcher": null
      "status": 房间状态
      "conductor": ?
      "checkNote": ?
      "userNote": ?
      "checkDate": ?
      "launchDate": ?
      "execDate": ?
      管理员预约的launcher为null
     * @apiSuccessExample {json} 响应数据示例
     * {
     *     "success": true,
     *     "code": 200,
     *     "data": {
     *         "appointments": [
     *             {
     *                 "id": 3,
     *                 "begin": null,
     *                 "end": null,
     *                 "roomId": 1,
     *                 "launchTime": 1,
     *                 "launcher": "null",
     *                 "status": "receive",
     *                 "conductor": "admin",
     *                 "checkNote": null,
     *                 "userNote": null,
     *                 "checkDate": 0,
     *                 "launchDate": 0,
     *                 "execDate": null
     *             },
     *             {
     *                 "id": 18,
     *                 "begin": null,
     *                 "end": null,
     *                 "roomId": 1,
     *                 "launchTime": 3,
     *                 "launcher": "null",
     *                 "status": "receive",
     *                 "conductor": "admin",
     *                 "checkNote": null,
     *                 "userNote": null,
     *                 "checkDate": 0,
     *                 "launchDate": 1614155056000,
     *                 "execDate": "2021-02-26"
     *             }
     *         ]
     *     }
     * }
     */
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
     * @api {Get} /admin/appointment/availablePeriod 管理员获取可用时间段
     * @apiGroup 管理员预约管理
     * @apiDescription 管理员获取可用时间段
     * @apiParam {Integer} roomId 待查询的房间id
     * @apiParam {String} conductor 发起预约的管理员 管理员发起的预约发起者launcher为空，审核者为管理员自身，故以conductor作为发起预约的管理员标识
     * @apiParam {String} date 待查询的日期，格式为yyyy-mm-dd 2021-10-07
     * @apiSuccessExample {json} 响应数据示例
     * {
     *     "success": true,
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": {
     *         "passTime": [
     *             {
     *                 "id": 1,
     *                 "begin": "07:00:00",
     *                 "end": "08:00:00"
     *             },
     *             {
     *                 "id": 2,
     *                 "begin": "08:00:00",
     *                 "end": "09:00:00"
     *             },
     *             {
     *                 "id": 3,
     *                 "begin": "09:00:00",
     *                 "end": "10:00:00"
     *             }
     *         ],
     *         "freeTime": [
     *             {
     *                 "id": 4,
     *                 "begin": "13:00:00",
     *                 "end": "14:00:00"
     *             }
     *         ],
     *         "myTime": [
     *             {
     *                 "id": 6,
     *                 "begin": "21:00:00",
     *                 "end": "22:00:00"
     *             }
     *         ],
     *         "busyTime": [
     *             {
     *                 "id": 5,
     *                 "begin": "14:00:00",
     *                 "end": "15:00:00"
     *             }
     *         ]
     *     }
     * }
     */
    @GetMapping("/availablePeriod")
    public CommonResult getAvailablePeriodByRoomId(@RequestParam(name = "roomId") Integer roomId, @RequestParam(name = "conductor") String conductor, @RequestParam(name = "date") String date) {
        Map result = roomService.getRoomFreeTimeByAdmin(roomId, conductor, date);
        return CommonResult.ok(ResultCode.SUCCESS)
                .data(result);
    }

    /**
     * @api {Get} /admin/appointment/allTime 管理员获取所有预约时间段
     * @apiGroup 管理员预约管理
     * @apiDescription 管理员获取所有预约时间段
     * @apiSuccessExample {json} 响应数据示例
     * {
     *   "code": 200,
     *   "message": "",
     *   "data": {
     *     "beginTime": {
     *       {"id": 1, "time": "08:00:00"},
     *       {"id": 2, "time": "10:00:00"},
     *       {"id": 3, "time": "13:00:00"},
     *       {"id": 4, "time": "15:00:00"},
     *       {"id": 5, "time": "18:00:00"},
     *       {"id": 6, "time": "21:00:00"}
     *     },
     *     "endTime": {
     *       {"id": 1, "time": "10:00:00"},
     *       {"id": 2, "time": "12:00:00"},
     *       {"id": 3, "time": "15:00:00"},
     *       {"id": 4, "time": "18:00:00"},
     *       {"id": 5, "time": "21:00:00"},
     *       {"id": 6, "time": "22:00:00"}
     *     }
     *   }
     * }
     */
    @GetMapping("/allTime")
    public CommonResult getAllTimeByAdmin() {
        List<Schedule> scheduleList = scheduleService.getAllTime();

        class scheduleBeginTime {
            private Integer id;
            private String beginTime;

            public scheduleBeginTime(Integer id, String beginTime) {
                this.id = id;
                this.beginTime = beginTime;
            }
        }

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
