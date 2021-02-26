package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.community.domain.Admin;
import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.service.AdminService;
import cn.edu.bit.ruixin.community.service.UserService;
import cn.edu.bit.ruixin.community.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/23
 */
@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public CommonResult login(@RequestBody(required = true)Admin admin) {
        Admin loginAdmin = adminService.login(admin);
        // 添加用户角色信息
        return CommonResult.ok(ResultCode.SUCCESS).msg("管理员登录成功!").data("userInfo", loginAdmin);
    }

    @GetMapping("/user/{username}")
    public CommonResult getUserInfo(@PathVariable("username") String username) {
        User user = userService.getUserByUsername(username);
        return CommonResult.ok(ResultCode.SUCCESS).data("userInfo", UserInfoVo.convertToVo(user));
    }
}
