package cn.edu.bit.ruixin.community.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@Order(1)                                                        
public class UserLogoutConfigure extends WebSecurityConfigurerAdapter {
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/user/logout")   
            .csrf().disable()
            .logout(logout -> logout
                .permitAll()
                .logoutUrl("/user/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("JSESSIONID")
            )
            .httpBasic();
    }
}