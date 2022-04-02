package cn.edu.bit.ruixin.community.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * 预约通过模板，模板id{@code F5-LCFCIXt04AY0WPSC0vJAQsjyFXOn2vltNKXs5ABQ}
 *
 * @author jingkaimori
 * @date 2021/8/24
 * @see WxMessageTemplateVo
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class WxAppointmentPassTemplateVo extends WxMessageTemplateVo {
    private WxMessageTFieldVo name5;
    private WxMessageTFieldVo thing2;
    private WxMessageTFieldVo date3;
    private WxMessageTFieldVo date4;
    private WxMessageTFieldVo phrase8;
    
    public WxAppointmentPassTemplateVo(){
        this.name5 = new WxMessageTFieldVo();
        this.thing2 = new WxMessageTFieldVo();
        this.date3 = new WxMessageTFieldVo();
        this.date4 = new WxMessageTFieldVo();
        this.phrase8 = new WxMessageTFieldVo();
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
    @Override
    public String getType() {
        return "F5-LCFCIXt04AY0WPSC0vJAQsjyFXOn2vltNKXs5ABQ";
    }
}