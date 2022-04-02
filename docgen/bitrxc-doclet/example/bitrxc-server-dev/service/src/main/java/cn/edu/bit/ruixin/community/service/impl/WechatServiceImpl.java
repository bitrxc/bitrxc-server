package cn.edu.bit.ruixin.community.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cn.edu.bit.ruixin.community.domain.WxAppAccessVo;
import cn.edu.bit.ruixin.community.domain.WxAppProperties;
import cn.edu.bit.ruixin.community.domain.WxAppResultVo;
import cn.edu.bit.ruixin.community.domain.WxAppVO;
import cn.edu.bit.ruixin.community.domain.WxMessageTemplateVo;
import cn.edu.bit.ruixin.community.service.WechatService;

@Service
public class WechatServiceImpl implements WechatService {

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private WxAppProperties appProperties;
    
    /** JSON字符串处理工具 */
    @Autowired
    private ObjectMapper mapper;

    private WxAppAccessVo accessVo = null;

    private Date expireTime = new Date();

    /**
     * 通知微信用户
     */
    @Override
    public void notifyWechatUser(String openid,WxMessageTemplateVo appointmentinfo) throws RuntimeException  {
        // 将字符编码调整为utf-8
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        WxAppAccessVo accessVo = ensureAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessVo.getAccess_token();
        // 封装HTTP请求
        // 请求头，其实是一种多值Map
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 封装请求体，json类型
        Map<String, Object> body = new HashMap<>();
        body.put("touser", openid);
        body.put("template_id", appointmentinfo.getType());
        body.put("miniprogram_state", "develop");
        body.put("data", appointmentinfo);

        String valueAsString;
        try {
            valueAsString = mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("服务器异常，请联系开发人员以寻求帮助！",e);
        }

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(valueAsString, headers);
        ResponseEntity<WxAppResultVo> response = restTemplate.postForEntity(url, request, WxAppResultVo.class);
        // 获取响应体
        WxAppResultVo responseBody = response.getBody();
        if (responseBody == null) {
            throw new RuntimeException("服务器异常，请重试！");
        }else if(responseBody.getErrcode() != 0){
            throw new RuntimeException("服务器异常，请重试！\n"+responseBody.getErrmsg());
        }
    }

    /**
     * 向微信后台请求access_token，并强制刷新已存储的token，代码复制自 {@link cn.edu.bit.ruixin.community.aop.MsgCheck} 
     * @author jingkaimori
     * @throws RuntimeException
     */
    @Override
    public WxAppAccessVo getAccessToken() throws RuntimeException {
        // 由于微信的后台api相对稳定 额外的配置代码会引入其他问题 因此硬编码可以接受
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appProperties.appId+"&secret="+appProperties.secret;
        String accessResponse = restTemplate.getForObject(url, String.class);
        // 获取access_token
        // 处理上游服务器格式异常
        try{
            WxAppAccessVo accessVo = mapper.readValue(accessResponse, WxAppAccessVo.class);
            return accessVo;
        }catch(JsonProcessingException e){
            throw new RuntimeException("服务器异常，请联系开发人员以寻求帮助！",e);
        }
    }
    
    /** 请求微信后台的敏感词检验 */ 
    @Override
    public Boolean checkString(String content) throws RuntimeException {
        WxAppAccessVo accessVo = ensureAccessToken();
        String token = accessVo.getAccess_token();
        String postUrl = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token="+token;
        // 封装HTTP请求
        // 请求头，其实是一种多值Map
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 封装请求体，json类型
        Map<String, String> map = new HashMap<>();
        map.put("content", content);
        String valueAsString;
        try {
            valueAsString = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("服务器异常，请联系开发人员以寻求帮助！",e);
        }

        HttpEntity<String> request = new HttpEntity<>(valueAsString, headers);
        ResponseEntity<WxAppResultVo> response = restTemplate.postForEntity(postUrl, request, WxAppResultVo.class);
        // 获取响应体
        WxAppResultVo responseBody = response.getBody();
        if (responseBody == null) {
            throw new RuntimeException("服务器异常，请重试！");
        }
        return responseBody.getErrcode() != 87014;
        
    }
    
    private WxAppAccessVo ensureAccessToken() throws RuntimeException {
        if(expireTime.before(new Date())){ //token 过期
            accessVo = getAccessToken();
            expireTime.setTime(expireTime.getTime() + (accessVo.getExpires_in()-5)*1000);
        }else{}
        return accessVo;
    }

    @Override
    public WxAppVO login(String tempcode) throws RuntimeException {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+ appProperties.appId +"&secret="+appProperties.secret+"&js_code="+tempcode+"&grant_type=authorization_code";
        String object = restTemplate.getForObject(url, String.class);

        try{
            WxAppVO appVO = mapper.readValue(object, WxAppVO.class);
            return appVO;
        }catch(JsonProcessingException e){
            throw new RuntimeException("登录失败，请重试！亦可联系开发人员以寻求帮助",e);
        }
    }
}
