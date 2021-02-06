package cn.edu.bit.ruixin.community.filter;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.base.security.utils.ResponseUtils;
import cn.edu.bit.ruixin.base.security.utils.TokenManager;
import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.vo.UserInfoVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/5
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private TokenManager tokenManager;

    public TokenLoginFilter(AuthenticationManager authenticationManager, TokenManager tokenManager) {
        this.tokenManager = tokenManager;
        this.authenticationManager = authenticationManager;
    }

    /**
     * 获取表单提交的用户名、密码，用于登录认证
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserInfoVo userInfoVo = new ObjectMapper().readValue(request.getInputStream(), UserInfoVo.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userInfoVo.getUsername(), userInfoVo.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException("服务器异常，请重试!");
        }
    }

    /**
     * 认证成功后调用方法
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String token = tokenManager.createToken(user.getUsername());
        // 保存token，用户权限到Redis

        // 返回Token给前端
        ResponseUtils.out(response, CommonResult.ok(ResultCode.SUCCESS).msg("登录成功!").data("token", token));
    }

    /**
     * 认证失败后调用方法
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ResponseUtils.out(response, CommonResult.error(ResultCode.UNAUTHENTICATION_ERROR).msg("认证失败了!"));
    }
}
