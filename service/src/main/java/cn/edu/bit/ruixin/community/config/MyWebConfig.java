package cn.edu.bit.ruixin.community.config;

import cn.edu.bit.ruixin.community.filter.GlobalLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.servlet.Filter;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/19
 */
@Configuration
public class MyWebConfig {

    @Autowired
    private GlobalLoginFilter loginFilter;

    @Bean
    public FilterRegistrationBean registerBean() {
        System.out.println(loginFilter);
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(loginFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("globalAuthenticationFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
