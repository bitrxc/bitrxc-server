package cn.edu.bit.ruixin.community.config.secure;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import cn.edu.bit.ruixin.base.security.utils.UnauthorizedEntryPoint;
import cn.edu.bit.ruixin.community.filter.TokenAdminFilter;
import cn.edu.bit.ruixin.community.service.PermissionService;
import cn.edu.bit.ruixin.community.service.RedisService;
import cn.edu.bit.ruixin.community.service.RoleService;

/**
 * 管理员普通接口配置，需要鉴权
 * 由于过滤器无法自动注入依赖项，在此处为过滤器注入依赖
 * 
 * @author jingkaimori
 * @date 2021/07/18
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@Order(4)                                                        
public class AdminConfigure extends WebSecurityConfigurerAdapter {
    
    @Autowired(required = true)
    private RedisService redisService;

    @Autowired(required = true)
    private PermissionService permissionService;
    
    @Autowired(required = true)
    private RoleService roleService;
    
    protected void configure(final HttpSecurity http) throws Exception {
        http
            .cors(withDefaults())
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(new UnauthorizedEntryPoint())) 
            .antMatcher("/admin/**")
            .csrf().disable()
            .authorizeRequests(authorize -> authorize
                .anyRequest()
                .authenticated()
                /* .hasRole("ADMIN")*/)
            // .httpBasic(withDefaults());
            .addFilterAt(
                new TokenAdminFilter(redisService,permissionService,roleService), 
                BasicAuthenticationFilter.class);
    }
}