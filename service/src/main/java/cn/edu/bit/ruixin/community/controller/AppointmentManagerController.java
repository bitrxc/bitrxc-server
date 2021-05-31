package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.community.annotation.MsgSecCheck;
import cn.edu.bit.ruixin.community.domain.Appointment;
import cn.edu.bit.ruixin.community.domain.Room;
import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.myenum.AppointmentStatus;
import cn.edu.bit.ruixin.community.service.AppointmentService;
import cn.edu.bit.ruixin.community.service.RoomService;
import cn.edu.bit.ruixin.community.service.UserService;
import cn.edu.bit.ruixin.community.vo.AppointmentInfoVo;
import cn.edu.bit.ruixin.community.vo.RoomInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @GetMapping("")
    public CommonResult lookupAppointmentById(@RequestParam("id")Integer id) {
        Appointment appointment = appointmentService.getAppointmentById(id);

        AppointmentInfoVo infoVo = AppointmentInfoVo.convertToVo(appointment);
        User user = userService.getUserByUsername(infoVo.getLauncher());
        Room room = roomService.getRoomInfoById(infoVo.getRoomId());
        infoVo.setPhone(user.getPhone());
        infoVo.setUsername(user.getName());
        infoVo.setRoomName(room.getName());
        infoVo.setSchoolId(user.getSchoolId());

        return CommonResult.ok(ResultCode.SUCCESS).data("appointment", infoVo);
    }

    @GetMapping("/{current}/{limit}/{schoolId}")
    public CommonResult lookupAppointmentBySchoolId(@PathVariable("current")int current,@PathVariable("limit") int limit, @PathVariable("schoolId") String schoolId) {
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
            infoVo.setPhone(user.getPhone());
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
    public CommonResult getAllAppointment(@RequestParam(required = false, name = "status")String status) {
        List<Appointment> list = appointmentService.getAllAppointment(status);
        List<AppointmentInfoVo> infoVos = new ArrayList<>();

        for (Appointment appointment:list
             ) {
            AppointmentInfoVo infoVo = AppointmentInfoVo.convertToVo(appointment);
            User user = userService.getUserByUsername(infoVo.getLauncher());
            Room room = roomService.getRoomInfoById(infoVo.getRoomId());
            infoVo.setPhone(user.getPhone());
            infoVo.setSchoolId(user.getSchoolId());
            infoVo.setUsername(user.getName());
            infoVo.setRoomName(room.getName());
            infoVos.add(infoVo);
        }
        return CommonResult.ok(ResultCode.SUCCESS).data("appointments", infoVos);
    }

    /**
     * 分页查询
     * @param current
     * @param limit
     * @return
     */
    @GetMapping("/{current}/{limit}")
    public CommonResult getAppointmentPages(@PathVariable("current") int current, @PathVariable("limit") int limit, @RequestParam(required = false, name = "status")String status) {
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
            infoVo.setPhone(user.getPhone());
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

    @MsgSecCheck({"conductor", "checkNote"})
    @PutMapping("/check/{appointmentId}")
    public CommonResult check(@PathVariable(name = "appointmentId")Integer id,
                               @RequestParam(required = true, name = "status")String status,
                               @RequestParam(required = true, name = "conductor")String conductor,
                              @RequestParam("checkNote") String checkNote) {
        appointmentService.checkOutAppointment(id, status, conductor, checkNote);
        return CommonResult.ok(ResultCode.SUCCESS).msg("审批操作成功!");
    }
}
