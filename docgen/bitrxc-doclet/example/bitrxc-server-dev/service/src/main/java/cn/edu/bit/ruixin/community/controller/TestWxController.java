package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.community.annotation.MsgSecCheck;
import cn.edu.bit.ruixin.community.domain.WxAppResultVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/10
 */
@RestController
@RequestMapping("/test")
public class TestWxController {

    @MsgSecCheck({"vo", "name"})
    @PostMapping("/msgCheck")
    public CommonResult checkDemo(@RequestBody String vo) {
        System.out.println(vo);
        return CommonResult.ok(ResultCode.SUCCESS);
    }

    @RequestMapping("/code")
    public CommonResult getWx(@RequestParam("code") String code) {

        return CommonResult.ok(ResultCode.SUCCESS);
    }
}
