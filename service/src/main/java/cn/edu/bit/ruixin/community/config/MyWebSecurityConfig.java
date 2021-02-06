package cn.edu.bit.ruixin.community.config;

import cn.edu.bit.ruixin.base.security.utils.TokenLogoutHandler;
import cn.edu.bit.ruixin.base.security.utils.TokenManager;
import cn.edu.bit.ruixin.base.security.utils.UnauthorizedEntryPoint;
import cn.edu.bit.ruixin.community.filter.TokenBasicFilter;
import cn.edu.bit.ruixin.community.filter.TokenLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Spring Security配置类
 *
 * @author 78165
 * @date 2021/2/5
 */
@Configuration
@EnableWebSecurity
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("myUserDetailsServiceImpl")
    private UserDetailsService userDetailsService;
    @Autowired
    @Qualifier("defaultPasswordEncoder")
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/user/register");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(new UnauthorizedEntryPoint())
                .and().csrf().disable()
                .authorizeRequests().anyRequest().authenticated()
                .and().logout().permitAll().logoutUrl("/user/logout")
                .logoutSuccessHandler(logoutSuccessHandler).deleteCookies("JSESSIONID").and()
                .formLogin()
                .permitAll()
//                .loginProcessingUrl("/user/login") // 需修改实现的UsernamePasswordAuthenticationFilter
//                .successHandler(authenticationSuccessHandler)
//                .failureHandler(authenticationFailureHandler)
                .and()
                .addFilterAt(new TokenLoginFilter(authenticationManager(), tokenManager), UsernamePasswordAuthenticationFilter.class) //添加到过滤器链指定位置
                .addFilterAt(new TokenBasicFilter(authenticationManager(), tokenManager), BasicAuthenticationFilter.class)
                .httpBasic();
    }
}
