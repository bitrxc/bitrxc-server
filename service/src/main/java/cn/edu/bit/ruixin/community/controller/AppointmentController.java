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
@RequestMapping("/appointment")
@CrossOrigin
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;


    @GetMapping("/username/{username}")
    public CommonResult lookupAllAppointment(@PathVariable(name = "username")String username) {
        List<Appointment> list = appointmentService.getAllAppointmentByUsername(username);
        List<AppointmentInfoVo> infoVos = new ArrayList<>();
        for (Appointment appointment:list
             ) {
            infoVos.add(AppointmentInfoVo.convertToVo(appointment));
        }
        return CommonResult.ok(ResultCode.SUCCESS).data("appointments", infoVos);
    }

    @GetMapping("/id/{id}")
    public CommonResult lookupAppointmentById(@PathVariable(name = "id")Integer id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        return CommonResult.ok(ResultCode.SUCCESS).data("appointment", AppointmentInfoVo.convertToVo(appointment));
    }

    @PostMapping("/appoint")
    public CommonResult appoint(@RequestBody(required = true)AppointmentInfoVo infoVo) {
        Appointment appointment = AppointmentInfoVo.convertToPo(infoVo);
        appointmentService.addANewAppointment(appointment);
        return CommonResult.ok(ResultCode.SUCCESS).msg("预约成功!");
    }

    @PutMapping("/cancel/{appointmentId}")
    public CommonResult cancel(@PathVariable(name = "appointmentId")Integer id) {
        appointmentService.cancelAppointmentById(id);
        return CommonResult.ok(ResultCode.SUCCESS).msg("撤销预约成功!");
    }
}
