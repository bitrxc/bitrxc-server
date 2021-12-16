package cn.edu.bit.ruixin.community.aop;

import cn.edu.bit.ruixin.community.annotation.FieldNeedCheck;
import cn.edu.bit.ruixin.community.annotation.MsgSecCheck;
import cn.edu.bit.ruixin.community.domain.WxAppAccessVo;
import cn.edu.bit.ruixin.community.domain.WxAppResultVo;
import cn.edu.bit.ruixin.community.exception.UserDaoException;
import cn.edu.bit.ruixin.community.service.WechatService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 使用AOP进行敏感词检查，使用前置通知，使用注解定义切入点，
 * 根据注解传递需要进行检测的参数，对类上要检查的字段加注解标记
 *
 * @author 78165
 * @date 2021/4/5
 */
@Component
@Aspect
public class MsgCheck {

    @Autowired
    private WechatService wechatService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    /**
     * 注意切入点表达式的写法
     * @param joinPoint
     * @param msgSecCheck 拿到切入点的特定注解
     */
    @Before(value = "@annotation(cn.edu.bit.ruixin.community.annotation.MsgSecCheck)&&@annotation(msgSecCheck)")
    public void beforeExecute(JoinPoint joinPoint, MsgSecCheck msgSecCheck) {
//        System.out.println("进入切面");
        try {
            // 向微信后台请求access_token
            WxAppAccessVo accessVo = wechatService.getAccessToken();

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
                    flag = checkString((String) arg,accessVo.getAccess_token());
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
                            flag = checkString((String) field.get(arg),accessVo.getAccess_token());
                            if(!flag){
                                break;
                            }
                        }
                    }
                    if (!flag) break;
                }
            }

            if (!flag) throw new UserDaoException("您上传的内容含有敏感成分，请检查！");

        } catch (JsonProcessingException | IllegalAccessException e) {
            throw new RuntimeException("服务器出错，请重试！");
        }
    }

    /** 请求微信后台的敏感词检验 */ 
    private Boolean checkString(String content,String token) throws JsonProcessingException {
        
        String postUrl = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token="+token;
        // 封装HTTP请求
        // 请求头，其实是一种多值Map
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 封装请求体，json类型
        Map<String, String> map = new HashMap<>();
        map.put("content", content);
        String valueAsString = mapper.writeValueAsString(map);

        HttpEntity<String> request = new HttpEntity<>(valueAsString, headers);
        ResponseEntity<WxAppResultVo> response = restTemplate.postForEntity(postUrl, request, WxAppResultVo.class);
        // 获取响应体
        WxAppResultVo responseBody = response.getBody();
        if (responseBody == null) {
            throw new RuntimeException("服务器异常，请重试！");
        }
        return responseBody.getErrcode() != 87014;
        
    }
}
