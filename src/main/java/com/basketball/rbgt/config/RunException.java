package com.basketball.rbgt.config;


import com.basketball.rbgt.enums.ErrorCodeAndMsg;

/**
 * @program: rbgt
 * @author: 俞春旺
 * @create: 2020/06/12 21:58
 * @description： 描述：统一异常捕获类
 */
public class RunException extends RuntimeException{

    private static final long serialVersionUID = -6370612186038915645L;

    private final ErrorCodeAndMsg response;

    public RunException(ErrorCodeAndMsg response) {
        this.response = response;
    }
    public ErrorCodeAndMsg getResponse() {
        return response;
    }
}