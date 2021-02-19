//package cn.edu.bit.ruixin.base.security.utils;
//
//import cn.edu.bit.ruixin.base.common.CommonResult;
//import cn.edu.bit.ruixin.base.common.ResultCode;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
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
// * @date 2021/2/5
// */
//@Component
//public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
//    @Override
//    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//        ResponseUtils.out(httpServletResponse, CommonResult.error(ResultCode.UNAUTHENTICATION_ERROR));
//    }
//}
