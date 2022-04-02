
package cn.edu.bit.ruixin.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.community.domain.Appointment;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestWeb {
	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private TestRestTemplate restTemplate;
    
	@LocalServerPort
	private int port;

    @Test
    public void testLogin(){

        CommonResult res = restTemplate.getForObject(
            "http://localhost:" + port +"/admin/login", CommonResult.class);
        assertThat(res.getData())
            .extracting("token").isNotNull();
    }
    
    @Test
    public void testViewList(){
        CommonResult res = restTemplate.getForObject(
            "http://localhost:" + port +"/admin/login", CommonResult.class);
        RestTemplate template = restTemplateBuilder
            .defaultHeader("token", res.getData().get("token").toString())
            .build();
        template.getForObject("http://localhost:" + port +"/admin/appointment?id=52",CommonResult.class);
        assertThat(res.getData())
            .extracting("appointment").isInstanceOf(Appointment.class);
    }
}

class LoginReturn {
    public int code;
    public Boolean success;
    public String token;
}