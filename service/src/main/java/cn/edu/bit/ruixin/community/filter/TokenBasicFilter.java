//package cn.edu.bit.ruixin.community.filter;
//
//import cn.edu.bit.ruixin.base.security.utils.TokenManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//
///**
// * TODO
// *
// * @author 78165
// * @date 2021/2/6
// */
//public class TokenBasicFilter extends BasicAuthenticationFilter {
//
//    private TokenManager tokenManager;
//
//    public TokenBasicFilter(AuthenticationManager authenticationManager, TokenManager tokenManager) {
//        super(authenticationManager);
//        this.tokenManager = tokenManager;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        //获取当前认证成功用户权限信息
//        UsernamePasswordAuthenticationToken authRequest = getAuthentication(request);
//        //判断如果有权限信息，放到权限上下文中
//        if(authRequest != null) {
//            SecurityContextHolder.getContext().setAuthentication(authRequest);
//        }
//        chain.doFilter(request,response);
//    }
//
//    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
//        String token = request.getHeader("token");
//        if (token != null && !token.equals("")) {
//            String username = tokenManager.getInfoFromToken(token);
//
//            return new UsernamePasswordAuthenticationToken(username, token, new ArrayList<>());
//        }
//        return null;
//    }
//}
