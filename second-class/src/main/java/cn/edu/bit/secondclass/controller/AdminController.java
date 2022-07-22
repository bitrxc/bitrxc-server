package cn.edu.bit.secondclass.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {
    //TODO:管理员登录
    @PostMapping("/login")
    public CommonResult login() {

        return CommonResult.ok(ResultCode.SUCCESS);
    }
}
