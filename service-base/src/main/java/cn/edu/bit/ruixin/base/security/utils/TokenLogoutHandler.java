package cn.edu.bit.ruixin.base.security.utils;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/5
 */
@Component
public class TokenLogoutHandler implements LogoutHandler {
    /**
     * 退出登录处理器
     * @param request
     * @param response
     * @param authentication
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 1. 从header中获取token
        // 2. token不为空，移除token
        String token = request.getHeader("token");
        if (token != null) {
            // 移除token
        }
        ResponseUtils.out(response, CommonResult.ok(ResultCode.SUCCESS).msg("退出登录成功!"));
    }
}
