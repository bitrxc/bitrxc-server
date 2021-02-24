package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.community.domain.Admin;
import cn.edu.bit.ruixin.community.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/login")
    public CommonResult login(@RequestBody(required = true)Admin admin) {
        Admin loginAdmin = adminService.login(admin);
        return CommonResult.ok(ResultCode.SUCCESS).msg("管理员登录成功!").data("admin", loginAdmin);
    }
}
