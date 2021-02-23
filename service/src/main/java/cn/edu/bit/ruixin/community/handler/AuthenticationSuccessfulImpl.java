//package cn.edu.bit.ruixin.community.handler;
//
//import cn.edu.bit.ruixin.base.common.CommonResult;
//import cn.edu.bit.ruixin.base.common.ResultCode;
//import cn.edu.bit.ruixin.base.security.utils.ResponseUtils;
//import cn.edu.bit.ruixin.base.security.utils.TokenManager;
//import cn.edu.bit.ruixin.community.domain.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * TODO
// *
// * @author 78165
// * @date 2021/2/6
// */
//@Component
//public class AuthenticationSuccessfulImpl implements AuthenticationSuccessHandler {
//
//    @Autowired
//    private TokenManager tokenManager;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String token = tokenManager.createToken(user.getUsername());
//        // 保存token，用户权限到Redis
//
//        // 返回Token给前端
//        ResponseUtils.out(response, CommonResult.ok(ResultCode.SUCCESS).msg("登录成功!").data("token", token));
//
//    }
//}
