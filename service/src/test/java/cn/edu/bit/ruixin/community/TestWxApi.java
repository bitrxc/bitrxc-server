package cn.edu.bit.ruixin.community;

// import cn.edu.bit.ruixin.community.domain.WxAppAccessVo;
// import cn.edu.bit.ruixin.community.domain.WxAppResultVo;
// import cn.edu.bit.ruixin.community.repository.AppointmentRepository;
import cn.edu.bit.ruixin.community.service.WechatService;

// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.MediaType;
// import org.springframework.http.ResponseEntity;
// import org.springframework.util.LinkedMultiValueMap;
// import org.springframework.util.MultiValueMap;
// import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * TODO
 *
 * @author 78165
 * @author jingkaimori
 * @date 2021/2/10
 */
public class TestWxApi {
    @Autowired
    private WechatService wechatService;

    @Test
    public void testAccessToken() {
        assertDoesNotThrow(() -> 
            wechatService.getAccessToken()
        );
    }

    @Test
    public void testMessageCheck() {
        assertFalse(
            wechatService.checkString(
                "特3456书yuuo莞6543李zxcz蒜7782法fgnv级\n"+
                "完2347全dfji试3726测asad感3847知qwez到"
            )
        );
    }

    /**
     * 测试从微信临时登录码换取用户登录态的能力
     * 
     */
    @Deprecated
    public void testWxCode2Session() {
//         String url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
//         RestTemplate restTemplate = new RestTemplate();
// //        restTemplate.getForObject(url, )
    }

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

    @Deprecated
    public void testString() {
//        String s1 = "Hello";
//        String s2 = new String(s1);
//        System.out.println((s1 == s2));

        HashMap<String, String> map = new HashMap<>(32);
        map.put("a", "A");
        map.put("b", "B");
    }
}
