package cn.edu.bit.ruixin.community;

import cn.edu.bit.ruixin.base.security.utils.MapToBean;
import cn.edu.bit.ruixin.community.domain.Admin;
import cn.edu.bit.ruixin.community.service.RedisService;
import cn.edu.bit.ruixin.community.vo.AdminInfoVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Map;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/13
 */
@SpringBootTest
public class TestRedis {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisService redisService;

    @Test
    public void testRedis() {
//        redisTemplate.opsForValue().append("msg", "hello");
//        redisTemplate.opsForHash()
        Admin admin = new Admin();
        admin.setUsername("Kobe");
//        ObjectMapper mapper = new ObjectMapper();
//        String s = mapper.writeValueAsString(admin);
//        redisTemplate.opsForValue().set("admin", s);
//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwaG9uZSI6IjEyMzQ1NiIsImV4cCI6MTYxNTczMjk4OH0.tXPZ7aPwLLxDKbaEWeShR-D0SLocMtsyuBrTfkp49yk";
//        AdminInfoVo adminInfoVo = redisService.opsForValueGet(token, AdminInfoVo.class);
//        System.out.println(adminInfoVo);
//        redisTemplate.opsForValue().set("kobe", admin);
//        Object kobe = redisTemplate.opsForValue().get("kobe");
//        System.out.println(kobe);
//        System.out.println(kobe.getClass());
//        redisTemplate.opsForValue()
//        redisTemplate.delete("kobe");
//        Map<String, Object> map = MapToBean.beanToMap(admin);
//        System.out.println(map);
//        redisTemplate.opsForHash().putAll("kobe", map);
//        System.out.println(redisTemplate.opsForHash().get("kobe", "username"));
//        Map<Object, Object> entries = redisTemplate.opsForHash().entries("kobe");
//        System.out.println(entries.entrySet());
        Object o = redisTemplate.opsForHash().get("name", "name");

    }

    @Test
    public void test() throws FileNotFoundException {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        String absolutePath = path.getAbsolutePath();
        System.out.println(absolutePath);
        // 生成文件名，使用雪花算法生成全局ID
        String filename = null;
    }

    @Test
    public void testNio() throws IOException {
        FileInputStream inputStream = new FileInputStream(new File("C:\\Users\\78165\\Desktop\\image-20210305150804266.png"));
        byte[] buf = new byte[1024];
        FileOutputStream outputStream = new FileOutputStream(new File("C:\\Users\\78165\\Desktop\\test.png"));
        FileChannel channel = outputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (inputStream.read(buf) > 0) {
            buffer.put(buf);
            buffer.flip();
            channel.write(buffer);
            buffer.clear();
        }
    }
}
