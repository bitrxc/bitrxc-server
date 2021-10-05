package cn.edu.bit.ruixin.community.config.secure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import cn.edu.bit.ruixin.base.security.utils.TokenManager;
import cn.edu.bit.ruixin.community.filter.TokenBasicFilter;
import cn.edu.bit.ruixin.community.service.RedisService;

/**
 * 用户登出过滤器
 * 
 * @author jingkaimori
 * @date 2021/07/18
 */
@Configuration
@Order(3)                                                        
public class UserLogoutConfigure extends WebSecurityConfigurerAdapter {
    @Autowired
    TokenManager tokenManager;

    @Autowired
    RedisService redisService;
    // @Autowired
    // private LogoutSuccessHandler logoutSuccessHandler;

    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/user/logout")   
            .csrf().disable()
            .addFilterAt(
                new TokenBasicFilter(tokenManager,redisService), 
                BasicAuthenticationFilter.class)
            .authorizeRequests(authorize -> authorize
                .anyRequest().authenticated()
                /* .hasRole("ADMIN")*/)
            .logout(logout -> logout
                .logoutUrl("/user/logout")
                //.logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("JSESSIONID").permitAll()
            )
            .httpBasic();
    }
}