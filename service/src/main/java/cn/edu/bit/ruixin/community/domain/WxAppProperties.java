package cn.edu.bit.ruixin.community.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/19
 */
@Component
@Data
@ConfigurationProperties(prefix = "wx")
public class WxAppProperties {

    public String appId;

    public String secret;
}
