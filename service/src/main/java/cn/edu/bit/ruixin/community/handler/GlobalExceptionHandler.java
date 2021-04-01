package cn.edu.bit.ruixin.community.handler;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.community.exception.AppointmentDaoException;
import cn.edu.bit.ruixin.community.exception.FileUploadDownloadException;
import cn.edu.bit.ruixin.community.exception.RoomDaoException;
import cn.edu.bit.ruixin.community.exception.UserDaoException;
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

    @ExceptionHandler(FileUploadDownloadException.class)
    @ResponseBody
    public CommonResult error(FileUploadDownloadException e) {
        logger.error(e.getMessage());
        return CommonResult.error(ResultCode.INTERNAL_SERVER_ERROR).msg(e.getMessage());
    }

    @ExceptionHandler(AppointmentDaoException.class)
    @ResponseBody
    public CommonResult error(AppointmentDaoException e) {
        logger.error(e.getMessage());
        return CommonResult.error(ResultCode.INTERNAL_SERVER_ERROR).msg(e.getMessage());
    }

    @ExceptionHandler(RoomDaoException.class)
    @ResponseBody
    public CommonResult error(RoomDaoException e) {
        logger.error(e.getMessage());
        return CommonResult.error(ResultCode.INTERNAL_SERVER_ERROR).msg(e.getMessage());
    }

    @ExceptionHandler(UserDaoException.class)
    @ResponseBody
    public CommonResult error(UserDaoException e) {
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
