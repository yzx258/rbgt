package com.basketball.rbgt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.basketball.rbgt.config.Response;
import com.basketball.rbgt.mapper.EventMapper;
import com.basketball.rbgt.pojo.Event;
import com.basketball.rbgt.task.TaskUtil;
import com.basketball.rbgt.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin
@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private TaskUtil taskService;
    @Autowired
    private EventMapper eventMapper;

    @ApiOperation(value = "查询 - 当天赛事信息")
    @GetMapping("/today/event")
    public Response todayEvent(){
        QueryWrapper<Event> queryWrapper = new QueryWrapper<Event>();
        queryWrapper.eq("start_time",DateUtil.getDate(0));
        return new Response(eventMapper.selectList(queryWrapper));
    }

    @ApiOperation(value = "查询 - 明天赛事信息")
    @GetMapping("/tomorrow/event")
    public Response tomorrowEvent(){
        QueryWrapper<Event> queryWrapper = new QueryWrapper<Event>();
        queryWrapper.eq("start_time",DateUtil.getDate(1));
        return new Response(eventMapper.selectList(queryWrapper));
    }

    @ApiOperation(value = "查询 - 前天赛事信息")
    @GetMapping("/yesterday/event")
    public Response yesterdayEvent(){
        QueryWrapper<Event> queryWrapper = new QueryWrapper<Event>();
        queryWrapper.eq("start_time",DateUtil.getDate(-1));
        return new Response(eventMapper.selectList(queryWrapper));
    }

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

