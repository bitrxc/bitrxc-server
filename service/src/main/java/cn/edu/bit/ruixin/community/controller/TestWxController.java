package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/10
 */
@RestController
@RequestMapping("/test")
public class TestWxController {

    @RequestMapping("/code")
    public CommonResult getWx(@RequestParam("code") String code) {

        return CommonResult.ok(ResultCode.SUCCESS);
    }
}
