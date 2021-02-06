package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.service.UserService;
import cn.edu.bit.ruixin.community.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/5
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public CommonResult registerUser(@RequestBody(required = true)UserInfoVo infoVo) {
        User user = UserInfoVo.convertToPo(infoVo);
        userService.registerNewUser(user);
        return CommonResult.ok(ResultCode.SUCCESS).msg("注册成功!");
    }

    @PostMapping("/modify")
    public CommonResult modifyUser(@RequestBody(required = true)UserInfoVo infoVo) {
        User user = UserInfoVo.convertToPo(infoVo);
        userService.modifyUser(user);
        return CommonResult.ok(ResultCode.SUCCESS).msg("修改用户信息成功!");
    }

    @GetMapping("/userInfo/{username}")
    public CommonResult getUserInfo(@PathVariable("username") String username) {
        User user = userService.getUserByUsername(username);
        return CommonResult.ok(ResultCode.SUCCESS).data("userInfo", UserInfoVo.convertToVo(user));
    }
}
