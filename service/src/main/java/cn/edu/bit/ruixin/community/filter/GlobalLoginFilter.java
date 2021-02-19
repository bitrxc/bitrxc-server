package cn.edu.bit.ruixin.community.filter;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.base.security.utils.ResponseUtils;
import cn.edu.bit.ruixin.base.security.utils.TokenManager;
import cn.edu.bit.ruixin.community.domain.WxAppVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/19
 */
@Component
public class GlobalLoginFilter implements Filter {

    @Autowired
    private TokenManager tokenManager;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String servletPath = request.getServletPath();
        if (servletPath.equals("/user/login")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            // 检查是否已登录
            String token = request.getHeader("token");
            if (token != null) {
                HttpSession session = request.getSession();
                WxAppVO appVO = (WxAppVO) session.getAttribute("WeChatUserInfo");
                if (appVO != null) {

                    String openid = appVO.getOpenid();
                    String sessionKey = appVO.getSession_key();

                    Map<String, String> info = tokenManager.getInfoFromToken(token);
//                    System.out.println(info);
                    try {
                        if (info.get("openid").equals(openid) && info.get("session_key").equals(sessionKey)) {
                            filterChain.doFilter(servletRequest, servletResponse);
                        } else {
                            ResponseUtils.out((HttpServletResponse) servletResponse, CommonResult.error(ResultCode.UNAUTHENTICATION_ERROR).msg("登录过期，请重新登录！"));
                        }
                    } catch (Exception e) {
                        ResponseUtils.out((HttpServletResponse) servletResponse, CommonResult.error(ResultCode.UNAUTHENTICATION_ERROR).msg("登录过期，请重新登录！"));
                    }
                } else {
                    ResponseUtils.out((HttpServletResponse) servletResponse, CommonResult.error(ResultCode.UNAUTHENTICATION_ERROR).msg("登录过期，请重新登录！"));
                }
            } else {
                ResponseUtils.out((HttpServletResponse) servletResponse, CommonResult.error(ResultCode.UNAUTHENTICATION_ERROR).msg("请先登录！"));
            }
        }
    }
}
