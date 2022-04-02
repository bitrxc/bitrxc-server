package cn.edu.bit.ruixin.community;

import cn.edu.bit.ruixin.community.domain.WxAppAccessVo;
import cn.edu.bit.ruixin.community.domain.WxAppResultVo;
import cn.edu.bit.ruixin.community.repository.AppointmentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/10
 */
public class TestWxApi {

    @Test
    public void testHttpsRest() throws JsonProcessingException {
        RestTemplate template = new RestTemplate();
        // GET https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx807f40cbd0449570&secret=3849a95d2480f6425462a79db7b530da";
        String jsonObject = template.getForObject(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        WxAppAccessVo accessVo = mapper.readValue(jsonObject, WxAppAccessVo.class);
//        System.out.println(accessVo);
        // access_token=以QueryString写在URL中，其余参数JSON形式写在请求体中
        String postUrl = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token="+accessVo.getAccess_token();
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("content", "你好");
        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("content", "");
        String value = mapper.writeValueAsString(bodyMap);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(value, headers);
        ResponseEntity<WxAppResultVo> entity = template.postForEntity(postUrl, request, WxAppResultVo.class);
        System.out.println(entity);
    }


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

    @Test
    public void testTimeApi() throws ParseException {
        String date = "2021-02-25";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = dateFormat.parse(date);
        System.out.println(dateFormat.format(date1));
    }

    @Test
    public void testTimeCompare() throws InterruptedException {
        Date date = new Date();
        System.out.println(date);
        Thread.sleep(1000);
        Date date1 = new Date();
        System.out.println(date1);
        if (date1.after(date)) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }
    }

    @Test
    public void testCollections() {
        List<Integer> list = new ArrayList<>();
        list.add(5);
        list.add(89);
        list.add(2);
        Collections.sort(list);
        for (Integer i :
                list) {
            System.out.println(i);
        }
    }

    @Test
    public void testString() {
//        String s1 = "Hello";
//        String s2 = new String(s1);
//        System.out.println((s1 == s2));

        HashMap<String, String> map = new HashMap<>(32);
        map.put("a", "A");
        map.put("b", "B");
    }
}
