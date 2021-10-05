package cn.edu.bit.ruixin.community.config.secure;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
/**
 * 
 * Spring Security配置类
 * 
 * 关于多个过滤器链条的配置，参见{@link https://docs.spring.io/spring-security/site/docs/current/reference/html5/#multiple-httpsecurity}
 * 忽略用户注册接口，禁用所有请求的 csrf 过滤器
 * cors 配置只需要进行一次，故在此进行
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
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "HEAD", "POST", "PUT", "DELETE", "TRACE", "OPTIONS", "PATCH"
        ));
        configuration.setAllowedHeaders(Arrays.asList(
            "X-Requested-With", "content-type", "token"
        ));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
