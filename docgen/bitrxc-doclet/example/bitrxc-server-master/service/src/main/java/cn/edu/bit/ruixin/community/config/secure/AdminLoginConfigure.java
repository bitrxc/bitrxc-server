package cn.edu.bit.ruixin.community.config.secure;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import cn.edu.bit.ruixin.base.security.utils.UnauthorizedEntryPoint;
/**
 * 管理员登录过滤器
 * 放行所有请求，由管理员服务来生成并缓存凭据
 * 
 * @author jingkaimori
 * @date 2021/07/12
 */
@Configuration
@Order(2)                                                        
public class AdminLoginConfigure extends WebSecurityConfigurerAdapter {
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors(withDefaults())
            .antMatcher("/admin/login")
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(new UnauthorizedEntryPoint())) 
            .csrf().disable();
            // .authorizeRequests(authorize -> authorize
            //     .anyRequest()
            //     .authenticated()
            //     /* .hasRole("ADMIN")*/)
            // .httpBasic();
    }
}