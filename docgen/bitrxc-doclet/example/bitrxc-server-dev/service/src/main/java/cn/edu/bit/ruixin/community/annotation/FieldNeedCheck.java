package cn.edu.bit.ruixin.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对于某个对象而言，如果某个字段上有该注解，消息检查机制会检查该字段的内容
 * 该注解只能打在vo类对象上,不能打在domain类对象上
 * TODO
 *
 * @author 78165
 * @date 2021/4/6
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldNeedCheck {
}
