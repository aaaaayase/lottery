package com.yun.lottery.controller.handler;

import com.yun.lottery.common.errorcode.GlobalErrorCodeConstants;
import com.yun.lottery.common.exception.ControllerException;
import com.yun.lottery.common.exception.ServiceException;
import com.yun.lottery.common.pojo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author yun
 * @date 2025/4/13 23:13
 * @desciption: 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public CommonResult<?> exceptionHandler(Exception e) {
        log.error("服务错误:", e);
        return CommonResult.error(
                GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(),
                e.getMessage());
    }
    @ExceptionHandler(value = ServiceException.class)
    public CommonResult<?> serviceExceptionHandler(ServiceException
                                                           serviceException) {
        log.error("serviceExceptionHandler:", serviceException);
        return CommonResult.error(
                GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(),
                serviceException.getMessage());
    }
    @ExceptionHandler(value = ControllerException.class)
    public CommonResult<?> controllerExceptionHandler(ControllerException
                                                              controllerException) {
        log.error("controllerExceptionHandler:", controllerException);
        return CommonResult.error(
                GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode(),
                controllerException.getMessage());
    }
}