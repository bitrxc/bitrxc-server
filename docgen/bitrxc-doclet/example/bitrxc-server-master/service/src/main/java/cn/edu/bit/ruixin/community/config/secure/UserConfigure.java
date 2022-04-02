package cn.edu.bit.ruixin.community.config.secure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import cn.edu.bit.ruixin.base.security.utils.TokenManager;
import cn.edu.bit.ruixin.base.security.utils.UnauthorizedEntryPoint;
import cn.edu.bit.ruixin.community.filter.TokenBasicFilter;
import cn.edu.bit.ruixin.community.service.RedisService;

/**
 * 用户普通接口配置，需要验证登录
 * 由于过滤器无法自动注入依赖项，在此处为过滤器注入依赖
 * 
 * @author jingkaimori
 * @date 2021/07/18
 */
@Configuration
@Order(5)                                                        
public class UserConfigure extends WebSecurityConfigurerAdapter {
    @Autowired
    TokenManager tokenManager;

    @Autowired
    RedisService redisService;
    
    protected void configure(HttpSecurity http) throws Exception {

        http
            .exceptionHandling(exception -> exception
            .authenticationEntryPoint(new UnauthorizedEntryPoint())) 
            .csrf().disable()
            .addFilterAt(
                new TokenBasicFilter(tokenManager,redisService), 
                BasicAuthenticationFilter.class)
            .authorizeRequests(authorize -> authorize
                .anyRequest().authenticated()
                )
            .httpBasic();
    }
}