package cn.edu.bit.ruixin.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import cn.edu.bit.ruixin.base.security.utils.UnauthorizedEntryPoint;
import cn.edu.bit.ruixin.community.filter.TokenBasicFilter;

@Configuration
@Order(3)                                                        
public class UserConfigure extends WebSecurityConfigurerAdapter {

    
    protected void configure(HttpSecurity http) throws Exception {

        http
            .exceptionHandling(exception -> exception
            .authenticationEntryPoint(new UnauthorizedEntryPoint())) 
            .csrf().disable()
            .addFilterAt(
                new TokenBasicFilter(authenticationManager()), 
                BasicAuthenticationFilter.class)
            .authorizeRequests(authorize -> authorize
                .anyRequest().authenticated()
                /* .hasRole("ADMIN")*/)
            .httpBasic();
    }
}