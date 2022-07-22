package cn.edu.bit.secondclass.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.secondclass.service.PointExchangedRecordService;
import cn.edu.bit.secondclass.vo.PointExchangedRecordInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/pointExchangedRecord")
@RestController
@CrossOrigin
public class PointExchangedRecordController {
    @Autowired
    private PointExchangedRecordService pointExchangedRecordService;

    @GetMapping("/all")
    public CommonResult queryAllPointExchangedRecords(@RequestParam(required = false) String status) {
        List<PointExchangedRecordInfoVo> records = pointExchangedRecordService.getAllPointExchangedRecords(status);
        return CommonResult.ok(ResultCode.SUCCESS)
                .data("records", records);
    }

    @GetMapping("/{schoolId}")
    public CommonResult queryAllPointExchangedRecordsBySchoolId(@PathVariable(name = "schoolId") String schoolId
            , @RequestParam(name = "status") String status) {
        List<PointExchangedRecordInfoVo> records = pointExchangedRecordService
                .getPointExchangedRecordByUserIdAndStatus(schoolId, status);
        return CommonResult.ok(ResultCode.SUCCESS)
                .data("records", records);
    }
}
