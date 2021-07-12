package cn.edu.bit.ruixin.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import cn.edu.bit.ruixin.base.security.utils.UnauthorizedEntryPoint;

@Configuration
@Order(2)                                                        
public class AdminConfigure extends WebSecurityConfigurerAdapter {
    protected void configure(HttpSecurity http) throws Exception {
        http
            .exceptionHandling(exception -> exception
            .authenticationEntryPoint(new UnauthorizedEntryPoint())) 
            .antMatcher("/admin/**")
            .csrf().disable()
            .authorizeRequests(authorize -> authorize
                .anyRequest()
                .authenticated()
                /* .hasRole("ADMIN")*/)
            .httpBasic();
    }
}