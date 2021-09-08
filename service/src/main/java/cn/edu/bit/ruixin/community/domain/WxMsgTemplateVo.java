package cn.edu.bit.ruixin.community.domain;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信订阅消息的模板参数，模板id{@code F5-LCFCIXt04AY0WPSC0vJAQsjyFXOn2vltNKXs5ABQ}
 * 由于列表格式复杂，故手工编写setter
 *
 * @author jingkaimori
 * @date 2021/8/24
 */
public class WxMsgTemplateVo {
    public WxMsgTemplateVo(){
        this.name5 = new Wrapper<String>();
    }

    public void setName5(String arg) {
        this.name5.setValue(arg);
    }
    public void setThing2(String arg) {
        this.thing2.setValue(arg);
    }
    public void setPhrase8(String arg) {
        this.phrase8.setValue(arg);
    }
    public void setDate3(Date arg) {
        this.date3.setValue(arg);
    }
    public void setDate4(Date arg) {
        this.date4.setValue(arg);
    }
    private Wrapper<String> name5;
    private Wrapper<String> thing2;
    private Wrapper<Date> date3;
    private Wrapper<Date> date4;
    private Wrapper<String> phrase8;
}

@Data
@NoArgsConstructor
class Wrapper<T>{
    public T value;
}