//package cn.edu.bit.ruixin.community.handler;
//
//import cn.edu.bit.ruixin.base.common.CommonResult;
//import cn.edu.bit.ruixin.base.common.ResultCode;
//import cn.edu.bit.ruixin.base.security.utils.ResponseUtils;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
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
//public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
//        ResponseUtils.out(response, CommonResult.error(ResultCode.UNAUTHENTICATION_ERROR).msg("认证失败!"));
//    }
//}
