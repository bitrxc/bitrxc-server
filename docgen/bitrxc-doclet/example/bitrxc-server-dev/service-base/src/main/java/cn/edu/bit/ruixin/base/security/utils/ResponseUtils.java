package cn.edu.bit.ruixin.base.security.utils;

import cn.edu.bit.ruixin.base.common.CommonResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/5
 */
public class ResponseUtils {

    public static void out(HttpServletResponse response, CommonResult result) {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=utf-8");
        try {
            mapper.writeValue(response.getWriter(), result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
