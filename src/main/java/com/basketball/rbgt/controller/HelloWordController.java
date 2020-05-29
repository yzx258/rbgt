package com.basketball.rbgt.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "/hello",tags = "测试 - demo")
@RestController
@RequestMapping("/test")
public class HelloWordController {

    @ApiOperation(value = "查询 - demo")
    @GetMapping("/hello")
    public String Test(){
        return "hello word";
    }

}
