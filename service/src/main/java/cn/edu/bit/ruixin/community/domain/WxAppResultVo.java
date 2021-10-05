package cn.edu.bit.ruixin.community.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 只返回错误码的微信接口的返回类型
 *
 * @author 78165
 * @date 2021/4/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxAppResultVo {
    private int errcode;
    private String errmsg;
}
