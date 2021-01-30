package cn.edu.bit.ruixin.spacemanager.handler;

import cn.edu.bit.ruixin.spacemanager.common.CommonResult;
import cn.edu.bit.ruixin.spacemanager.common.ResultCode;
import cn.edu.bit.ruixin.spacemanager.exception.RoomDaoException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理类，处理Controller发生的特定异常
 *
 * @author 78165
 * @date 2021/1/29
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RoomDaoException.class)
    @ResponseBody
    public CommonResult error(RoomDaoException e) {
        logger.error(e.getMessage());
        return CommonResult.error(ResultCode.INTERNAL_SERVER_ERROR).msg(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResult error(Exception e) {
        logger.error(e.getMessage());
        return CommonResult.error(ResultCode.INTERNAL_SERVER_ERROR).msg(e.getMessage());
    }
}
