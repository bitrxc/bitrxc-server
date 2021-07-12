package cn.edu.bit.ruixin.community.filter;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.base.security.utils.ResponseUtils;
import cn.edu.bit.ruixin.base.security.utils.TokenManager;
import cn.edu.bit.ruixin.community.domain.Permission;
import cn.edu.bit.ruixin.community.domain.Role;
import cn.edu.bit.ruixin.community.domain.WxAppVO;
import cn.edu.bit.ruixin.community.service.PermissionService;
import cn.edu.bit.ruixin.community.service.RedisService;
import cn.edu.bit.ruixin.community.service.RoleService;
import cn.edu.bit.ruixin.community.vo.AdminInfoVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
* 管理员 token 过滤器，
* 由于通过 token 校验并刷新会话时间，无法直接使用HTTP Plain Auth的处理方法
*  （{@link BasicAuthenticationFilter}）
* 实质上的凭据管理器为{@link RedisService}，但该管理器无法嵌入上述过滤器
* 目前鉴权模块继承于 filter 内
* 
* @author jingkaimori
* @date 2021/07/12
*/
public class TokenAdminFilter extends OncePerRequestFilter {

    @Autowired
    private RedisService redisService;

    @Autowired
    private PermissionService permissionService;
    
    @Autowired
    private RoleService roleService;

    public TokenAdminFilter() {
        // super(authenticationManager);
        // this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain chain
    ) throws IOException, ServletException {
        String token = request.getHeader("token");
        if (token != null && !token.equals("")) {
            try{
                // 获取登录状态
                AdminInfoVo adminInfoVo = redisService.opsForValueGet(token, AdminInfoVo.class);
                String servletPath = request.getServletPath();
                // 鉴权
                if (checkPrivilege(adminInfoVo, servletPath)) {
                    // 更新活动状态
                    redisService.updateExpire(token, 30, TimeUnit.MINUTES);
                    chain.doFilter(request, response);
                } else {
                    ResponseUtils.out(
                        (HttpServletResponse) response, 
                        CommonResult.error(ResultCode.NOAHTHORITY));
                }
            } catch (Exception e) {
                ResponseUtils.out(
                    (HttpServletResponse) response, 
                    CommonResult.error(ResultCode.UNAUTHENTICATION_ERROR).msg("登录过期，请重新登录！"));
            }
        }else{
            ResponseUtils.out(
                (HttpServletResponse) response, 
                CommonResult.error(ResultCode.UNAUTHENTICATION_ERROR).msg("请先登录！"));
        }
    }


    private class InvalidTokenException extends Exception{

    }

    /**
     * 
     * Check Admin's Privilege.
     * {@link https://shimo.im/docs/e1Az42LLOOcENEqW }
     * 
     * @param adminInfoVo
     * @param servletPath
     * @return
     * @throws MalformedURLException
     */
    private Boolean checkPrivilege(AdminInfoVo adminInfoVo,String servletPath) throws MalformedURLException{
        List<Role> roles = roleService.getRolesByAdminId(adminInfoVo.getId());
        String path = (new URL(servletPath)).getPath();
        System.out.println(path);
        Permission perm = permissionService.getPermissionByURL(path);
        return permissionService.checkPermission(perm, roles);
    }
}
