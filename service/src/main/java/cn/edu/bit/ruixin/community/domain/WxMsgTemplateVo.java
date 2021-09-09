package cn.edu.bit.ruixin.community.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信订阅消息的模板参数，模板id{@code F5-LCFCIXt04AY0WPSC0vJAQsjyFXOn2vltNKXs5ABQ}
 * 由于列表格式复杂，而且date转为字符串的格式特殊，故手工编写setter
 *
 * @author jingkaimori
 * @date 2021/8/24
 */
@Data
public class WxMsgTemplateVo {
    public WxMsgTemplateVo(){
        this.name5 = new Wrapper<String>();
        this.thing2 = new Wrapper<String>();
        this.date3 = new Wrapper<String>();
        this.date4 = new Wrapper<String>();
        this.phrase8 = new Wrapper<String>();
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(arg);
        this.date3.setValue(date);
    }
    public void setDate4(Date arg) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(arg);
        this.date4.setValue(date);
    }
    private Wrapper<String> name5;
    private Wrapper<String> thing2;
    private Wrapper<String> date3;
    private Wrapper<String> date4;
    private Wrapper<String> phrase8;
}

@Data
@NoArgsConstructor
class Wrapper<T>{
    public T value;
}