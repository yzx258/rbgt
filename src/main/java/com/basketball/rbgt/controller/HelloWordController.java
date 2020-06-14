package com.basketball.rbgt.controller;

import com.basketball.rbgt.task.TaskUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "/hello",tags = "测试 - demo")
@RestController
@RequestMapping("/test")
public class HelloWordController {


    @Autowired
    TaskUtil taskService;

    @ApiOperation(value = "查询 - demo")
    @GetMapping("/hello")
    public String Test(){
        System.out.println("异步线程开始");
        taskService.test();
        System.out.println("异步线程结束");
        return "hello word";
    }
}
