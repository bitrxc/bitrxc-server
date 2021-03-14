package cn.edu.bit.ruixin.base.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
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
    // Token有效时间：1天
    private Long tokenExpiration = 24*60*60L;
    // 编码密钥
    private String tokenSignKey = "123456";

    // 管理员端token生成
    public String createTokenForAdmin(String phone) {
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
                .withClaim("phone", phone)
                .sign(algorithm);
    }

    // 根据token字符串得到管理员信息
    public String getAdminInfoFromToken(String token) {
        DecodedJWT decode = JWT.decode(token);
        return decode.getClaim("phone").asString();
    }

    // 根据用户名生成token
    public String createToken(String openId, String sessionKey) {
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
                .withClaim("openid", openId)
                .withClaim("session_key", sessionKey)
                .sign(algorithm);
    }


    // 根据token字符串得到用户信息
    public Map<String, String> getInfoFromToken(String token) {
        DecodedJWT decode = JWT.decode(token);
        Map<String, String> info = new HashMap<>();
        info.put("openid", decode.getClaim("openid").asString());
        info.put("session_key", decode.getClaim("session_key").asString());
        return info;
    }
}
