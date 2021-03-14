package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.community.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/13
 */
@Service
public class RedisServiceImpl implements RedisService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void opsForValueSet(String key, Object value) throws JsonProcessingException {
        String valueAsString = objectMapper.writeValueAsString(value);
        redisTemplate.opsForValue().append(key, valueAsString);
    }

    @Override
    public void opsForValueSetWithExpire(String key, Object value, long timeout, TimeUnit unit) throws JsonProcessingException {
        String valueAsString = objectMapper.writeValueAsString(value);
        redisTemplate.opsForValue().set(key, valueAsString, timeout, unit);
    }

    @Override
    public <T> T opsForValueGet(String key, Class<T> clazz) throws JsonProcessingException {
        String obj = (String) redisTemplate.opsForValue().get(key);
        T value = objectMapper.readValue(obj, clazz);
        if (value != null) {
            return value;
        } else {
            return null;
        }
    }

    public void opsForHashSet(String key, Object object) throws JsonProcessingException {
        String valueAsString = objectMapper.writeValueAsString(object);
    }
}
