package cn.edu.bit.ruixin.community;

import cn.edu.bit.ruixin.community.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/10
 */
public class TestWxApi {


    // GET https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
    @Test
    public void testWxCode2Session() {
        // wxbcae8e38b4d93b28
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getForObject(url, )
    }

    @Test
    public void testJpa() {

    }
}
