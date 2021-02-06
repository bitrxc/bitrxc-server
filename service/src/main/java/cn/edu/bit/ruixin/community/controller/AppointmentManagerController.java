package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.community.domain.Appointment;
import cn.edu.bit.ruixin.community.service.AppointmentService;
import cn.edu.bit.ruixin.community.vo.AppointmentInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/lookup/appointment/{id}")
    public CommonResult lookupAppointmentById(@PathVariable(name = "id")Integer id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        return CommonResult.ok(ResultCode.SUCCESS).data("appointment", AppointmentInfoVo.convertToVo(appointment));
    }

    @GetMapping("/all")
    public CommonResult getAllAppointment(@RequestParam(required = false, name = "status")String status) {
        List<Appointment> list = appointmentService.getAllAppointment(status);
        List<AppointmentInfoVo> infoVos = new ArrayList<>();

        for (Appointment appointment:list
             ) {
            infoVos.add(AppointmentInfoVo.convertToVo(appointment));
        }
        return CommonResult.ok(ResultCode.SUCCESS).data("appointments", infoVos);
    }

    @PutMapping("/check/{appointmentId}")
    public CommonResult cancel(@PathVariable(name = "appointmentId")Integer id,
                               @RequestParam(required = true, name = "status")String status,
                               @RequestParam(required = true, name = "conductor")String conductor) {
        appointmentService.checkOutAppointment(id, status, conductor);
        return CommonResult.ok(ResultCode.SUCCESS).msg("审批操作成功!");
    }
}
