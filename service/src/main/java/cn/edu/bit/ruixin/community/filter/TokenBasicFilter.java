package cn.edu.bit.ruixin.community.filter;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.base.security.utils.ResponseUtils;
import cn.edu.bit.ruixin.base.security.utils.TokenManager;
import cn.edu.bit.ruixin.community.domain.WxAppVO;
import cn.edu.bit.ruixin.community.service.RedisService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户 token 过滤器， 由于通过 token 校验会话，而且需要更新会话时间，无法直接使用HTTP Plain
 * Auth的处理方法（{@link BasicAuthenticationFilter}）
 * 实质上的凭据管理器为{@link RedisService}，但该管理器无法嵌入上述过滤器
 * 过滤器要将成功的认证放入安全上下文
 *
 * @author 78165
 * @author jingkaimori
 * @date 2021/07/12
 */
public class TokenBasicFilter extends OncePerRequestFilter {

    private TokenManager tokenManager;

    private RedisService redisService;

    public TokenBasicFilter(TokenManager tokenManager,RedisService redisService) {
        this.tokenManager = tokenManager;
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.debug("triggered UserF");
        String token = request.getHeader("token");
        if (StringUtils.hasText(token)) {
            try {
                UsernamePasswordAuthenticationToken authRequest = getAuthentication(token, chain);
                // 将用户凭据放到认证权限上下文中
                SecurityContextHolder.getContext().setAuthentication(authRequest);
                chain.doFilter(request, response);
            } catch (Exception e) {
                ResponseUtils.out((HttpServletResponse) response,
                        CommonResult.error(ResultCode.UNAUTHENTICATION_ERROR).msg("登录过期，请重新登录！"));
            }
        } else {
            ResponseUtils.out((HttpServletResponse) response,
                    CommonResult.error(ResultCode.UNAUTHENTICATION_ERROR).msg("请先登录！"));
        }
    }

    /**
     * 认证当前用户，通过 redis 缓存机制保证会话时间
     * 
     * @return 当前认证成功用户会话信息
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String token, FilterChain filterChain)
            throws JsonProcessingException, InvalidTokenException {
        WxAppVO appVO = redisService.opsForValueGet(token, WxAppVO.class);
        if (appVO != null) {
            String openid = appVO.getOpenid();
            String sessionKey = appVO.getSession_key();
            Map<String, String> info = tokenManager.getInfoFromToken(token);
            if (info.get("openid").equals(openid) && info.get("session_key").equals(sessionKey)) {
                redisService.opsForValueSetWithExpire(token, appVO, 30, TimeUnit.MINUTES);
                return new UsernamePasswordAuthenticationToken(openid, token, new ArrayList<>());
            } else {
                throw new InvalidTokenException();
            }
        } else {
            throw new InvalidTokenException();
        }
    }

    private class InvalidTokenException extends Exception { }

}
