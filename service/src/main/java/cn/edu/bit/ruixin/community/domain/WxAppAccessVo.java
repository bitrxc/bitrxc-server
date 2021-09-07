package cn.edu.bit.ruixin.community.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务器的接口调用凭据( access_token )接口的返回类型
 * TODO
 *
 * @author 78165
 * @date 2021/4/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxAppAccessVo {
    private String access_token;
    private long expires_in;
    private int errcode;
    private String errmsg;
}
