package cn.edu.bit.ruixin.community.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 敏感词检查结果的返回类型
 * TODO
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
