package com.smartboy.zktlsloginbackend.global;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.smartboy.zktlsloginbackend.bean.CommonResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xuda
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CommonResponse<?> runtimeException(Exception e) {
        log.error("error: ", e);
        return CommonResponse.failure("-1", e.getMessage());
    }

}
