package cn.edu.bit.ruixin.community.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
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
