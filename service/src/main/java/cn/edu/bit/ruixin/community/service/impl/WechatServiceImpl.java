package cn.edu.bit.ruixin.community.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cn.edu.bit.ruixin.community.domain.WxAppAccessVo;
import cn.edu.bit.ruixin.community.domain.WxAppProperties;
import cn.edu.bit.ruixin.community.domain.WxAppResultVo;
import cn.edu.bit.ruixin.community.domain.WxMsgTemplateVo;
import cn.edu.bit.ruixin.community.service.WechatService;

@Service
public class WechatServiceImpl implements WechatService {

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private WxAppProperties appProperties;
    
    @Autowired
    private ObjectMapper mapper;

    private WxAppAccessVo accessVo = null;

    private Date expireTime = new Date();

    /**
     * 通知微信用户
     */
    @Override
    public void notifyWechatUser(String openid,WxMsgTemplateVo appointmentinfo) throws JsonMappingException, JsonProcessingException  {
        // TODO Auto-generated method stub
        WxAppAccessVo accessVo = ensureAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessVo.getAccess_token();
        // 封装HTTP请求
        // 请求头，其实是一种多值Map
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 封装请求体，json类型
        Map<String, Object> body = new HashMap<>();
        body.put("touser", openid);
        body.put("template_id", "F5-LCFCIXt04AY0WPSC0vJAQsjyFXOn2vltNKXs5ABQ");
        body.put("miniprogram_state", "develop");
        body.put("data", appointmentinfo);

        String valueAsString = mapper.writeValueAsString(body);

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
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    @Override
    public WxAppAccessVo getAccessToken() throws JsonMappingException, JsonProcessingException {
        // 后期加入配置文件中，不使用硬编码
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appProperties.appId+"&secret="+appProperties.secret;
        String accessResponse = restTemplate.getForObject(url, String.class);
        // 获取access_token
        WxAppAccessVo accessVo = mapper.readValue(accessResponse, WxAppAccessVo.class);
        return accessVo;
    }
    
    private WxAppAccessVo ensureAccessToken() throws JsonMappingException, JsonProcessingException {
        if(expireTime.before(new Date())){ //token 过期
            accessVo = getAccessToken();
            expireTime.setTime(expireTime.getTime() + (accessVo.getExpires_in()-5)*1000);
        }else{}
        return accessVo;
    }
}
