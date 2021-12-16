package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.community.domain.Schedule;
import cn.edu.bit.ruixin.community.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/4/19
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/schedule")
public class ScheduleManagerController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 返回所有时间段。当前接口不被前端使用
     * @see AppointManagerController.getAllTimeByAdmin
     * @return
     */
    @PreAuthorize("hasAuthority('appointCheck')")
    @GetMapping("/all")
    public CommonResult getAllTime() {
        List<Schedule> allTime = scheduleService.getAllTime();
        return CommonResult.ok(ResultCode.SUCCESS).data("timeList", allTime);
    }
}