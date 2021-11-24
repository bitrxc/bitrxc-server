package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.community.domain.Schedule;
import cn.edu.bit.ruixin.community.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/24
 */
@RestController
@CrossOrigin
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * @api {Get} /schedule/all 获取所有时间段
     * @apiGroup user interface: get all available time
     * @apiDescription 获取所有时间段
     * @apiSuccess {Array} timelist 时间表
     */
    @GetMapping("/all")
    public CommonResult getAllTime() {
        List<Schedule> allTime = scheduleService.getAllTime();
        return CommonResult.ok(ResultCode.SUCCESS).data("timeList", allTime);
    }
}
