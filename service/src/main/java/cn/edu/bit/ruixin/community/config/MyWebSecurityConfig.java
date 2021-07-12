package cn.edu.bit.ruixin.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 
 * Spring Security配置类
 * 
 * 关于多个过滤器链条的配置，参见{@link https://docs.spring.io/spring-security/site/docs/current/reference/html5/#multiple-httpsecurity}
 * 忽略用户注册接口，禁用所有请求的 csrf 过滤器
 * 
 * @author 78165
 * @author jingkaimori
 * @date 2021/7/12
 */
@Configuration
@EnableWebSecurity
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/user/register");
    }
}
