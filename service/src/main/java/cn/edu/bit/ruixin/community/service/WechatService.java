package cn.edu.bit.ruixin.community.service;

import cn.edu.bit.ruixin.community.domain.WxAppAccessVo;
import cn.edu.bit.ruixin.community.domain.WxAppVO;
import cn.edu.bit.ruixin.community.domain.WxMessageTemplateVo;

public interface WechatService {
    public void notifyWechatUser(String openid,WxMessageTemplateVo appointmentinfo) throws RuntimeException;

    public WxAppAccessVo getAccessToken() throws RuntimeException;

    public Boolean checkString(String content) throws RuntimeException;

    public WxAppVO login(String tempcode) throws RuntimeException;
}
