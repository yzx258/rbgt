package com.basketball.rbgt.config;

/**
 * @program: rbgt
 * @author: 俞春旺
 * @create: 2020/06/12 21:55
 * @description： 描述：
 */
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 请求返回类
 * Created by Tiger on 2018/10/9.
 */
@Getter
@Setter
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class Response<T> implements Serializable {

    private static final long serialVersionUID = -4505655308965878999L;

    //请求成功返回码为：200
    private static final String SUCCESS_CODE = "200";
    //返回数据
    private T data;
    //返回码
    private String code;
    //返回描述
    private String msg;

    public Response(){
        this.code = SUCCESS_CODE;
        this.msg = "请求成功";
    }

    public Response(String code,String msg){
        this();
        this.code = code;
        this.msg = msg;
    }
    public Response(String code,String msg,T data){
        this();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public Response(T data){
        this();
        this.data = data;
    }
}