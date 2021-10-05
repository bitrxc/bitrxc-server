package cn.edu.bit.ruixin.community.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.edu.bit.ruixin.community.domain.WxAppAccessVo;
import cn.edu.bit.ruixin.community.domain.WxMessageTemplateVo;

public interface WechatService {
    public void notifyWechatUser(String openid,WxMessageTemplateVo appointmentinfo) throws JsonMappingException, JsonProcessingException, RuntimeException;

    public WxAppAccessVo getAccessToken() throws JsonMappingException, JsonProcessingException;
}
