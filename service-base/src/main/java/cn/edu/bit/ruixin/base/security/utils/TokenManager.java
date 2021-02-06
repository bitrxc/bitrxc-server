package cn.edu.bit.ruixin.base.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/5
 */
@Component
public class TokenManager {
    // Token有效时间
    private Long tokenExpiration = 24*60*60*100L;
    // 编码密钥
    private String tokenSignKey = "123456";

    // 根据用户名生成token
    public String createToken(String wxid) {
        Date expirationTime = new Date(System.currentTimeMillis() + this.tokenExpiration);

        // 私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(tokenSignKey);

        // 设置头信息
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return JWT.create()
                .withHeader(header)
                .withExpiresAt(expirationTime)
                .withAudience(wxid) // 添加wxid负载信息
                .sign(algorithm);
    }


    // 根据token字符串得到用户信息
    public String getInfoFromToken(String token) {
        return JWT.decode(token).getAudience().get(0);
    }
}
