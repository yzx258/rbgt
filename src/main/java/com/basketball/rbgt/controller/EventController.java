package com.basketball.rbgt.controller;


import com.basketball.rbgt.task.TaskUtil;
import com.basketball.rbgt.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 俞春旺
 * @since 2020-06-30
 */
@Api(value = "/event",tags = "篮球赛事相关接口")
@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private TaskUtil taskService;

    @ApiOperation(value = "查询 - 手动查询当天赛事")
    @GetMapping("/today")
    public String today(){
        System.out.println("异步线程开始");
        taskService.getBasketballTournament(DateUtil.getDate(0));
        System.out.println("异步线程结束");
        return "hello word";
    }

    @ApiOperation(value = "查询 - 手动查询明天赛事")
    @GetMapping("/tomorrow")
    public String tomorrow(){
        System.out.println("异步线程开始");
        taskService.getBasketballTournament(DateUtil.getDate(1));
        System.out.println("异步线程结束");
        return "hello word";
    }

    @ApiOperation(value = "查询 - 手动查询昨天赛事")
    @GetMapping("/yesterday")
    public String yesterday(){
        System.out.println("异步线程开始");
        taskService.getBasketballTournament(DateUtil.getDate(-1));
        System.out.println("异步线程结束");
        return "hello word";
    }

}

