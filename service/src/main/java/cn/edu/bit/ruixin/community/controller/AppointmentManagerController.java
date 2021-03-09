package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.community.domain.Appointment;
import cn.edu.bit.ruixin.community.domain.Room;
import cn.edu.bit.ruixin.community.service.AppointmentService;
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

    @GetMapping("")
    public CommonResult lookupAppointmentById(@RequestParam("id")Integer id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        return CommonResult.ok(ResultCode.SUCCESS).data("appointment", AppointmentInfoVo.convertToVo(appointment));
    }

    @GetMapping("all")
    public CommonResult getAllAppointment(@RequestParam(required = false, name = "status")String status) {
        List<Appointment> list = appointmentService.getAllAppointment(status);
        List<AppointmentInfoVo> infoVos = new ArrayList<>();

        for (Appointment appointment:list
             ) {
            infoVos.add(AppointmentInfoVo.convertToVo(appointment));
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
        Sort sort = Sort.by(Sort.Direction.ASC, "execDate", "launchTime", "launchDate");
        // 构造分页对象
        Pageable pageable = PageRequest.of(current, limit, sort);
        Page<Appointment> page = appointmentService.getAppointmentPages(pageable, status);
        List<Appointment> list = page.getContent();

        List<AppointmentInfoVo> infoVos = new ArrayList<>();
        for (Appointment appointment :
                list) {
            infoVos.add(AppointmentInfoVo.convertToVo(appointment));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("totalElements", page.getTotalElements());
        map.put("totalPages", page.getTotalPages());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        map.put("items", infoVos);
        return CommonResult.ok(ResultCode.SUCCESS).data(map);
    }

    @PutMapping("/check/{appointmentId}")
    public CommonResult check(@PathVariable(name = "appointmentId")Integer id,
                               @RequestParam(required = true, name = "status")String status,
                               @RequestParam(required = true, name = "conductor")String conductor,
                              @RequestParam("checkNote") String checkNote) {
        appointmentService.checkOutAppointment(id, status, conductor);
        return CommonResult.ok(ResultCode.SUCCESS).msg("审批操作成功!");
    }
}
