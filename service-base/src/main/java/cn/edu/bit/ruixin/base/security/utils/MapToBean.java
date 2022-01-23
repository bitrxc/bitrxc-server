package cn.edu.bit.ruixin.base.security.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 在Java类型的对象和Map键值对之间转换对象的工具方法
 *
 * @author 78165
 * @author jingkaimori
 * @date 2022/1/23
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

    public static <T> T mapToBean(Map<String, Object> map, Class<T> tClass) throws 
        NoSuchMethodException,InvocationTargetException, IllegalAccessException, InstantiationException
    {
        T instance = tClass.getConstructor().newInstance();
        Field[] fields = tClass.getDeclaredFields();
        for (Field field :
                fields) {
            field.setAccessible(true);
            field.set(instance, map.getOrDefault(field.getName(), null));
        }
        return instance;
    }
}
