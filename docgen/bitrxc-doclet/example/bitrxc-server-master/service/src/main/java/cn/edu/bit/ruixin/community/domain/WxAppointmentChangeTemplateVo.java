package cn.edu.bit.ruixin.community.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 预约变更模板，模板id{@code F5-LCFCIXt04AY0WPSC0vJAQsjyFXOn2vltNKXs5ABQ}
 *
 * @author jingkaimori
 * @date 2021/10/05
 * @see WxMessageTemplateVo
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class WxAppointmentChangeTemplateVo extends WxMessageTemplateVo {
    private WxMessageTFieldVo thing17;
    private WxMessageTFieldVo thing9;
    private WxMessageTFieldVo date2;
    private WxMessageTFieldVo phrase16;

    public WxAppointmentChangeTemplateVo(){
        this.thing17 = new WxMessageTFieldVo();
        this.thing9 = new WxMessageTFieldVo();
        this.date2 = new WxMessageTFieldVo();
        this.phrase16 = new WxMessageTFieldVo();
    }

    public void setThing17(String arg) {
        this.thing17.setValue(arg);
    }
    public void setThing9(String arg) {
        this.thing9.setValue(arg);
    }
    public void setPhrase16(String arg) {
        this.phrase16.setValue(arg);
    }
    public void setDate2(Date arg) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(arg);
        this.date2.setValue(date);
    }

    @Override
    public String getType() {
        return "kk2mGYSeayqB7WqSKWhj34paWkf8wNQhvS_R5ChR0RE";
    }
}
