package cn.edu.bit.ruixin.base.security.utils;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/16
 */
public class MapToBean {
    
    public static Map<String, Object> beanToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> objClass = obj.getClass();
        Field[] fields = objClass.getDeclaredFields();
        for (Field field :
                fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            if (value == null) continue;
            map.put(fieldName, value);
        }
        return map;
    }

    public static <T> T mapToBean(Map<String, Object> map, Class<T> tClass) throws IllegalAccessException, InstantiationException {
        T instance = tClass.newInstance();
        Field[] fields = tClass.getDeclaredFields();
        for (Field field :
                fields) {
            field.setAccessible(true);
            field.set(instance, map.getOrDefault(field.getName(), null));
        }
        return instance;
    }
}
