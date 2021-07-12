package cn.edu.bit.ruixin.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 登录过滤器
 * 放行所有请求，由用户服务来生成并缓存凭据
 * 
 * @author jingkaimori
 * @date 2021/07/12
 */
@Configuration
@Order(1)                                                        
public class AdminLoginConfigure extends WebSecurityConfigurerAdapter {
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/admin/login")
            .csrf().disable()
            .authorizeRequests(authorize -> authorize
                .anyRequest()
                .authenticated()
                /* .hasRole("ADMIN")*/)
            .httpBasic();
    }
}