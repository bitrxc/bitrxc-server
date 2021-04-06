package cn.edu.bit.ruixin.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/4/5
 */
@Retention(RetentionPolicy.RUNTIME) // 注解保留策略，运行时保留
@Target(ElementType.METHOD)
public @interface MsgSecCheck {

    String[] value();
}
