package cn.edu.bit.ruixin.community.aop;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.edu.bit.ruixin.community.annotation.FieldNeedCheck;
import cn.edu.bit.ruixin.community.annotation.MsgSecCheck;
import cn.edu.bit.ruixin.community.exception.UserDaoException;
import cn.edu.bit.ruixin.community.service.WechatService;

/**
 * 使用AOP进行敏感词检查，使用前置通知，使用注解定义切入点，
 * 根据注解传递需要进行检测的参数，对类上要检查的字段加注解标记
 * TODO 利用Redis缓存已经检查过的字符串
 *
 * @author 78165
 * @date 2021/4/5
 */
@Component
@Aspect
public class MsgCheck {

    @Autowired
    private WechatService wechatService;

    /**
     * 注意切入点表达式的写法
     * @param joinPoint
     * @param msgSecCheck 拿到切入点的特定注解
     */
    @Before(value = "@annotation(cn.edu.bit.ruixin.community.annotation.MsgSecCheck)&&@annotation(msgSecCheck)")
    public void beforeExecute(JoinPoint joinPoint, MsgSecCheck msgSecCheck) {
//        System.out.println("进入切面");
        try {
            // 利用反射，获取切入点方法参数
            Object[] args = joinPoint.getArgs();
            // 获取方法签名
            MethodSignature pointSignature = (MethodSignature) joinPoint.getSignature();
            // 获取参数列表
            List<String> parameterNames = Arrays.asList(pointSignature.getParameterNames());
            // 获取需要进行敏感词过滤的参数，利用注解信息
            String[] valueNeedCheck = msgSecCheck.value();
            List<Integer> indexOfCheck = new ArrayList<>(valueNeedCheck.length);
            for (int i = 0; i < valueNeedCheck.length; i++) {
                // 获取需要检查的参数在方法签名参数列表的下标
                int index = parameterNames.indexOf(valueNeedCheck[i]);
                if (index != -1) {
                    indexOfCheck.add(index);
                }
            }
            // 对于每个类型的参数，获取需要检查的字段
            boolean flag = true; // 不含敏感词的标识
            for (Integer i :
                    indexOfCheck) {
                Object arg = args[i];
                // String 类型 直接进行检验
                if (arg instanceof String) {
                    flag = wechatService.checkString((String) arg);
                    if(!flag){
                        break;
                    }
                } else { // 自定义类型检查打了标记的字段
                    Class<?> argClass = arg.getClass();
                    // 获取参数类型下的所有字段
                    Field[] fields = argClass.getDeclaredFields();
                    for (Field field :
                            fields) {
                        FieldNeedCheck fnc = field.getAnnotation(FieldNeedCheck.class);
                        if (fnc != null && field.getType() == String.class) { // 打了检查标记注解，进行敏感词检查
                            field.setAccessible(true);
                            flag = wechatService.checkString((String) field.get(arg));
                            if(!flag){
                                break;
                            }
                        }
                    }
                    if (!flag) break;
                }
            }

            if (!flag) throw new UserDaoException("您上传的内容含有敏感成分，请检查！");

        } catch (IllegalAccessException e) {
            throw new RuntimeException("服务器出错，请重试！");
        }
    }

}
