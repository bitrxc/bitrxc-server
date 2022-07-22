package cn.edu.bit.secondclass.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.secondclass.domain.User;
import cn.edu.bit.secondclass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/id/{id}")
    public CommonResult getUserInfoById(@PathVariable(name = "id") int userId) {
        User user = userService.getUserById(userId);
        return CommonResult.ok(ResultCode.SUCCESS)
                .data("user", user);
    }

}
