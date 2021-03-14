package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.base.security.utils.TokenManager;
import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.domain.WxAppProperties;
import cn.edu.bit.ruixin.community.domain.WxAppVO;
import cn.edu.bit.ruixin.community.service.RedisService;
import cn.edu.bit.ruixin.community.service.UserService;
import cn.edu.bit.ruixin.community.vo.UserInfoVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
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
    private WxAppProperties wxAppProperties;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisService redisService;
    

    @GetMapping("/login")
    public CommonResult loginFromWeiXin(@RequestParam("code")String code) {

        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+ wxAppProperties.appId +"&secret="+wxAppProperties.secret+"&js_code="+code+"&grant_type=authorization_code";
        String object = restTemplate.getForObject(url, String.class);
        // JSON字符串处理工具
        Gson gson = new Gson();

        WxAppVO appVO = gson.fromJson(object, WxAppVO.class);

        if (appVO.getOpenid() != null) { // 微信后台认证成功，存入数据库，表示登录成功
            User user = userService.getUserByUsername(appVO.getOpenid());
            // 如果是第一次登陆，将微信用户信息存入数据库
            if (user == null) {
                user = new User();
                user.setUsername(appVO.getOpenid());
                userService.registerNewUser(user);
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
