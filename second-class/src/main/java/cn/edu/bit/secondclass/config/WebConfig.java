package cn.edu.bit.secondclass.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Configuration
@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
public class WebConfig {
    @Bean
    public ReadWriteLock readWriteLock() {
        return new ReentrantReadWriteLock();
    }
}
