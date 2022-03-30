package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.base.security.utils.TokenManager;
import cn.edu.bit.ruixin.community.annotation.MsgSecCheck;
import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.domain.WxAppVO;
import cn.edu.bit.ruixin.community.exception.ResourceNotFoundException;
import cn.edu.bit.ruixin.community.service.RedisService;
import cn.edu.bit.ruixin.community.service.UserService;
import cn.edu.bit.ruixin.community.service.WechatService;
import cn.edu.bit.ruixin.community.vo.UserInfoVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;


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

    @Autowired
    private WechatService wechatService;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private RedisService redisService;
    

    @GetMapping("/login")
    public CommonResult loginFromWeiXin(@RequestParam("code")String code) {
        WxAppVO appVO;
        // 此处的异常由微信服务封装
        appVO = wechatService.login(code);
        if (appVO.getOpenid() != null) { // 微信后台认证成功，存入数据库，表示登录成功
            try {
                userService.getUserByUsername(appVO.getOpenid());
            } catch (ResourceNotFoundException e) {
                // 如果是第一次登陆，将微信用户信息存入数据库
                userService.registerNewUser(appVO.getOpenid());
            }

            // 生成Token，用户系统身份凭证
            String token = tokenManager.createToken(appVO.getOpenid(), appVO.getSession_key());
            // 将Token及微信用户信息存入redis
            try {
                redisService.opsForValueSetWithExpire(token, appVO, 30, TimeUnit.MINUTES);

                return CommonResult.ok(ResultCode.SUCCESS).msg("登录成功！").data("token", token).data("openid", appVO.getOpenid());

            } catch (JsonProcessingException e) {
                // 抛出异常
                return CommonResult.error(ResultCode.INTERNAL_SERVER_ERROR).msg("登录失败，请重试！");
            }
        } else {
            return CommonResult.error(ResultCode.WECHATAUTHENTICATIONERROR);
        }
    }


    @MsgSecCheck("infoVo")
    @PostMapping("")
    public CommonResult modifyUser(@RequestBody(required = true)UserInfoVo infoVo) {
        User user = UserInfoVo.convertToPo(infoVo);
        userService.modifyUser(user);
        return CommonResult.ok(ResultCode.SUCCESS).msg("修改用户信息成功!");
    }

    @GetMapping("{username}")
    public CommonResult getUserInfo(@PathVariable("username") String username) {
        User user = userService.getUserByUsername(username);
        return CommonResult.ok(ResultCode.SUCCESS).data("userInfo", UserInfoVo.convertToVo(user));
    }
}
