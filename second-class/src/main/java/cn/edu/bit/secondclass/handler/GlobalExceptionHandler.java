package cn.edu.bit.secondclass.handler;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.secondclass.exception.ClassHourChangedRecordDaoException;
import cn.edu.bit.secondclass.exception.PointExchangedRecordDaoException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PointExchangedRecordDaoException.class)
    @ResponseBody
    public CommonResult error(PointExchangedRecordDaoException e) {
        logger.error(e.getMessage());
        return CommonResult.error(ResultCode.INTERNAL_SERVER_ERROR).msg(e.getMessage());
    }

    @ExceptionHandler(ClassHourChangedRecordDaoException.class)
    @ResponseBody
    public CommonResult error(ClassHourChangedRecordDaoException e) {
        logger.error(e.getMessage());
        return CommonResult.error(ResultCode.INTERNAL_SERVER_ERROR).msg(e.getMessage());

    }
}
