package com.github.bhjj.handler;

import com.github.bhjj.enumeration.ErrorCodeEnum;
import com.github.bhjj.exception.BusinessException;
import com.github.bhjj.resp.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.net.BindException;

/**
 * 通用异常处理器
 * @author ZhangXianDuo
 * @date 2025/3/8
 */
@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

    /**
     * 处理数据校验异常
     * */
    @ExceptionHandler(BindException.class)
    public Result<Void> handlerBindException(BindException e){
        log.error(e.getMessage(),e);
        return Result.fail(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR);
    }

    /**
     * 处理业务异常
     * */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handlerBusinessException(BusinessException e){
        log.error(e.getMessage(),e);
        return Result.fail(e.getErrorCodeEnum());
    }

    /**
     * 处理系统异常
     * */
    @ExceptionHandler(Exception.class)
    public Result<Void> handlerException(Exception e){
        log.error(e.getMessage(),e);
        return Result.error();
    }

}