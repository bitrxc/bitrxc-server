package cn.edu.bit.ruixin.community.filter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.base.security.utils.ResponseUtils;
import cn.edu.bit.ruixin.community.domain.Permission;
import cn.edu.bit.ruixin.community.service.PermissionService;
import cn.edu.bit.ruixin.community.service.RedisService;
import cn.edu.bit.ruixin.community.service.RoleService;
import cn.edu.bit.ruixin.community.vo.AdminInfoVo;

/**
 * 管理员 token 过滤器， 由于通过 token 请求头校验会话，而且需要刷新会话时间，无法直接使用HTTP Plain
 * Auth的处理方法（{@link BasicAuthenticationFilter}）
 * 实质上的凭据管理器为{@link RedisService}，但该管理器无法嵌入上述过滤器 目前鉴权模块继承于 filter 内
 * 将filter注册为component会导致过滤器链的顺序改变，详见{@link https://stackoverflow.com/questions/33537388/use-autowired-with-a-filter-configured-in-springboot}
 * 利用springboot的鉴权注解来装饰controller方法实现鉴权
 * 
 * @author jingkaimori
 * @date 2021/07/12
 */
public class TokenAdminFilter extends OncePerRequestFilter {

    private RedisService redisService;

    private PermissionService permissionService;

    public TokenAdminFilter(RedisService redisService, PermissionService permissionService, RoleService roleService) {
        this.redisService = redisService;
        this.permissionService = permissionService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = request.getHeader("token");
        if (StringUtils.hasText(token)) {
            try {
                // 获取登录状态
                AdminInfoVo adminInfoVo = redisService.opsForValueGet(token, AdminInfoVo.class);
                List<Permission> permlist = this.getPrivilege(adminInfoVo);

                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(adminInfoVo.getId(), adminInfoVo.getPassword(), permlist);
                // 将用户凭据放到认证权限上下文中，使得权限过滤器可以识别
                SecurityContextHolder.getContext().setAuthentication(authRequest);
                // 更新活动状态
                redisService.updateExpire(token, 30, TimeUnit.MINUTES);
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
     * 
     * Get Admin's Privilege.
     * 
     * @param adminInfoVo
     * @return
     */
    private List<Permission> getPrivilege(AdminInfoVo adminInfoVo){
        List<Permission> perms = permissionService.getPermissionsByAdmin(AdminInfoVo.convertToPo(adminInfoVo));
        return perms;
    }
}
