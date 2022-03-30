package cn.edu.bit.secondclass.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.secondclass.service.PointExchangedRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin/pointExchangedRecord")
@RestController
@CrossOrigin
public class PointExchangedRecordManagerController {
    @Autowired
    PointExchangedRecordService pointExchangedRecordService;

    @PostMapping("/check/{exchangedRecordId}")
    public CommonResult check(@PathVariable(name = "exchangedRecordId") int recordId,
                              @RequestParam(name = "status") String status,
                              @RequestParam(name = "conductor") Integer adminId,
                              @RequestParam(name = "checkNote") String checkNote) {
        pointExchangedRecordService.checkout(recordId, status, adminId, checkNote);
        return CommonResult.ok(ResultCode.SUCCESS)
                .msg("审批成功");
    }
}
