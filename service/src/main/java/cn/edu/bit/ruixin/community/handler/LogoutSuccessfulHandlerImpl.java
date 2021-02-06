package cn.edu.bit.ruixin.community.handler;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.base.security.utils.ResponseUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/6
 */
@Component
public class LogoutSuccessfulHandlerImpl implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 1. 从header中获取token
        // 2. token不为空，移除token
        String token = request.getHeader("token");
        if (token != null) {
            // 移除token
        }
        ResponseUtils.out(response, CommonResult.ok(ResultCode.SUCCESS).msg("退出登录成功!"));

    }
}
