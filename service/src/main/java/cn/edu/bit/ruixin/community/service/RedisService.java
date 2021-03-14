package cn.edu.bit.ruixin.community.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/13
 */
public interface RedisService {

    void opsForValueSet(String key, Object value) throws JsonProcessingException;

    void opsForValueSetWithExpire(String key, Object value, long timeout, TimeUnit unit) throws JsonProcessingException;

    <T> T opsForValueGet(String key, Class<T> clazz) throws JsonProcessingException;

    <T> T opsForHashGetAll(String key, Class<T> tClass) throws IllegalAccessException, InstantiationException;

    boolean updateExpire(String key, long timeout, TimeUnit unit);

    boolean opsForHashSetAll(String key, Map<String, Object> map, long timeout, TimeUnit unit);
}
