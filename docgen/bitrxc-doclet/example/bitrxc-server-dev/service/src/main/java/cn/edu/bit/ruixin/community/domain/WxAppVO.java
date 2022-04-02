package cn.edu.bit.ruixin.community.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小程序用户登录接口的返回类型
 * TODO
 *
 * @author 78165
 * @date 2021/2/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxAppVO {

    private String openid;
    private String session_key;
    private String unionid;
    private Integer errcode;
    private String errmsg;
}
