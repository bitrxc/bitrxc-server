package cn.edu.bit.ruixin.community.filter;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.base.security.utils.ResponseUtils;
import cn.edu.bit.ruixin.base.security.utils.TokenManager;
import cn.edu.bit.ruixin.community.domain.Permission;
import cn.edu.bit.ruixin.community.domain.Role;
import cn.edu.bit.ruixin.community.domain.WxAppVO;
import cn.edu.bit.ruixin.community.service.AdminService;
import cn.edu.bit.ruixin.community.service.PermissionService;
import cn.edu.bit.ruixin.community.service.RedisService;
import cn.edu.bit.ruixin.community.service.RoleService;
import cn.edu.bit.ruixin.community.vo.AdminInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RedisService redisService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

//        filterChain.doFilter(servletRequest, servletResponse);
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String servletPath = request.getServletPath();

        // 允许跨域
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,content-type,token");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
        
        if (servletPath.startsWith("/admin")) {
            if (servletPath.equals("/admin/login")) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                String token = request.getHeader("token");
                if (token!=null && !token.equals("")) {
                    // 获取登录状态
                    AdminInfoVo adminInfoVo = redisService.opsForValueGet(token, AdminInfoVo.class);
                    if (adminInfoVo != null) {
                        // 鉴权
                        if (checkPrivilege(adminInfoVo, servletPath)) {
                            // 更新活动状态
                            redisService.updateExpire(token, 30, TimeUnit.MINUTES);
                            filterChain.doFilter(servletRequest, servletResponse);
                        } else {
                            ResponseUtils.out((HttpServletResponse) servletResponse, CommonResult.error(ResultCode.NOAHTHORITY));
                        }
                    } else {
                        ResponseUtils.out((HttpServletResponse) servletResponse, CommonResult.error(ResultCode.UNAUTHENTICATION_ERROR).msg("登录过期，请重新登录！"));
                    }
                } else {
                    ResponseUtils.out((HttpServletResponse) servletResponse, CommonResult.error(ResultCode.UNAUTHENTICATION_ERROR).msg("请先登录！"));
                }
            }
        } else {
            if (servletPath.equals("/user/login")) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                // 检查是否已登录
                String token = request.getHeader("token");
                if (token != null) { // 已经登录，检查token
                    WxAppVO appVO = redisService.opsForValueGet(token, WxAppVO.class);
                    if (appVO != null) {

                        String openid = appVO.getOpenid();
                        String sessionKey = appVO.getSession_key();

                        Map<String, String> info = tokenManager.getInfoFromToken(token);

                        try {
                            if (info.get("openid").equals(openid) && info.get("session_key").equals(sessionKey)) {
                                redisService.opsForValueSetWithExpire(token, appVO, 30, TimeUnit.MINUTES);
                                // 检验登录状态成功，放行
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
//                filterChain.doFilter(servletRequest, servletResponse  );
            }
        }
    };

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
