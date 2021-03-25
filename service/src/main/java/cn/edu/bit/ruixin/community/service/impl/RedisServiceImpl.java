package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.community.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Map;
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
//        String valueAsString = objectMapper.writeValueAsString(value);
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void opsForValueSetWithExpire(String key, Object value, long timeout, TimeUnit unit) throws JsonProcessingException {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    @Override
    public <T> T opsForValueGet(String key, Class<T> clazz) throws JsonProcessingException {
        T value = (T) redisTemplate.opsForValue().get(key);
        if (value != null) {
            return value;
        } else {
            return null;
        }
    }

    @Override
    public <T> T opsForHashGetAll(String key, Class<T> tClass) throws IllegalAccessException, InstantiationException {
        if (redisTemplate.hasKey(key)) {
            T instance = tClass.newInstance();
            Field[] fields = tClass.getDeclaredFields();
            for (Field field :
                    fields) {
                field.setAccessible(true);
                Object value = redisTemplate.opsForHash().get(key, field.getName());
                field.set(instance, value);
            }
            return instance;
        } else {
            return null;
        }
    }

    // 更新key的过期时间，维护会话状态等
    @Override
    public boolean updateExpire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public boolean opsForHashSetAll(String key, Map<String, Object> map, long timeout, TimeUnit unit) {
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.expire(key, timeout, unit);
        return true;
    }

    public void opsForHashSet(String key, Object object) throws JsonProcessingException {
        String valueAsString = objectMapper.writeValueAsString(object);
    }
}
