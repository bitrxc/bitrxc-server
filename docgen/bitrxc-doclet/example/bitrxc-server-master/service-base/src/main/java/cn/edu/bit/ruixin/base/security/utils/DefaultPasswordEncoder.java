package cn.edu.bit.ruixin.base.security.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/5
 */
@Component
public class DefaultPasswordEncoder {
    /**
     * 密码编码器编码方法
     * @param charSequence : 源串
     * @return
     */
    public String encode(CharSequence charSequence) {
        // 使用Spring的MD5加密工具
        return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes());
    }

    /**
     * 将传入密码与数据库中密码进行比对
     * @param charSequence 传入源串
     * @param s 数据库中暗文密码
     * @return
     */
    public boolean matches(CharSequence charSequence, String s) {
        String pwd = encode(charSequence);
        return s.equals(pwd);
    }
}
