package cn.edu.bit.ruixin.community;

import cn.edu.bit.ruixin.community.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
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
