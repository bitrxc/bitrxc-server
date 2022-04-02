package cn.edu.bit.ruixin.community.config.secure;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import cn.edu.bit.ruixin.base.security.utils.UnauthorizedEntryPoint;

/**
 * 登录过滤器
 * 放行所有请求，由用户服务来生成并缓存凭据
 * 由于普通用户的认证为微信第三方认证，故不适用用户密码过滤器，也无需使用过滤器设置
 * 用户名和密码
 * 需要检查请求头是否包含 token 字段，此功能使用手写 filter 实现
 * 
 * @author jingkaimori
 * @date 2021/07/12
 */
@Configuration
@Order(1)                                                        
public class UserLoginConfigure extends WebSecurityConfigurerAdapter {        
    protected void configure(HttpSecurity http) throws Exception {
        http
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(new UnauthorizedEntryPoint())) 
            .antMatcher("/user/login")
            .csrf().disable()
            // .addFilterAt(
            //     new TokenLoginFilter(authenticationManager(), tokenManager), 
            //     UsernamePasswordAuthenticationFilter.class) //添加到过滤器链指定位置
            // .formLogin()
            // .permitAll() 
            .httpBasic();
    }
}