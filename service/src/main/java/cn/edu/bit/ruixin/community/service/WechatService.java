package cn.edu.bit.ruixin.community.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.edu.bit.ruixin.community.domain.WxAppAccessVo;
import cn.edu.bit.ruixin.community.domain.WxMsgTemplateVo;

public interface WechatService {
    public void notifyWechatUser(String openid,WxMsgTemplateVo appointment) throws JsonMappingException, JsonProcessingException ;

    public WxAppAccessVo getAccessToken() throws JsonMappingException, JsonProcessingException;
}
