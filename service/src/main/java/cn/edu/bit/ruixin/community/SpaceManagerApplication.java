package cn.edu.bit.ruixin.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan({"cn.edu.bit.ruixin"})
public class SpaceManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpaceManagerApplication.class, args);
    }

}
