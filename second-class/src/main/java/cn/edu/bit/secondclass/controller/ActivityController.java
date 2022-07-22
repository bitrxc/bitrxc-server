package cn.edu.bit.secondclass.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.secondclass.service.ActivityService;
import cn.edu.bit.secondclass.service.ClassHourChangedRecordService;
import cn.edu.bit.secondclass.vo.ActivityInfoVo;
import cn.edu.bit.secondclass.vo.ClassHourChangedRecordInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/activity")
@CrossOrigin
public class ActivityController {

    @Autowired
    private ClassHourChangedRecordService classHourChangedRecordService;

    @Autowired
    private ActivityService activityService;

    @PostMapping("/participate")
    public CommonResult participate(@RequestBody ClassHourChangedRecordInfoVo[] records,
                                    @RequestParam(name = "conductorId") Integer adminId,
                                    @RequestParam(name = "reason") String reason) {
        classHourChangedRecordService
                .updateUserClassHour(Arrays.stream(records).collect(Collectors.toList()), adminId, reason);
        return CommonResult.ok(ResultCode.SUCCESS);
    }

    @GetMapping("/all")
    public CommonResult queryAllActivity() {
        List<ActivityInfoVo> activities = activityService.getAllActivities();
        return CommonResult.ok(ResultCode.SUCCESS)
                .data("activities", activities);
    }

    @GetMapping("/number/{number}")
    public CommonResult queryActivityByNumber(@PathVariable(name = "number") String number) {
        ActivityInfoVo activity = activityService.getActivityByNumber(number);
        return CommonResult.ok(ResultCode.SUCCESS)
                .data("activity", activity);
    }

    @GetMapping("/id/{id}")
    public CommonResult queryActivityById(@PathVariable(name = "id") int id) {
        ActivityInfoVo activity = activityService.getActivityById(id);
        return CommonResult.ok(ResultCode.SUCCESS)
                .data("activity", activity);
    }
}
