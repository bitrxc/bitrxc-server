package cn.edu.bit.ruixin.community.domain;

/**
 * 微信订阅消息的消息体，不同的模板参数不同
 * 由于列表格式复杂，而且date转为字符串的格式特殊，故手工编写setter
 * 模板的参数详情可从微信后台查看
 * 添加新的通知模板后，请为此类型实现子类，以接入模板参数
 * 
 * @author jingkaimori
 * @date 2021/10/5
 */
public abstract class WxMessageTemplateVo {
    /**
     * 根据子类的类型，返回对应的模板ID
     * @return 该通知的消息体对应的模板ID
     */
    public abstract String getType();
}
