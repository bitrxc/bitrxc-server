package cn.edu.bit.ruixin.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 如果某方法带有此注解 执行此方法时，就检查该方法的输入参数
 * TODO
 *
 * @author 78165
 * @date 2021/4/5
 */
@Retention(RetentionPolicy.RUNTIME) // 注解保留策略，运行时保留
@Target(ElementType.METHOD)
public @interface MsgSecCheck {
    /** 需要检查的参数名列表 */
    String[] value();
}
