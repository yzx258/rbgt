package com.basketball.rbgt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.basketball.rbgt.config.Response;
import com.basketball.rbgt.mapper.EventMapper;
import com.basketball.rbgt.pojo.Event;
import com.basketball.rbgt.task.TaskUtil;
import com.basketball.rbgt.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "查询 - 根据日期查询赛事信息")
    @GetMapping("get/date/event/{date}")
    public Response todayEvent(@ApiParam(value = "date") @PathVariable String date){
        System.out.println("date - > "+date);
        QueryWrapper<Event> queryWrapper = new QueryWrapper<Event>();
        queryWrapper.eq("start_time",date);
        return new Response(eventMapper.selectList(queryWrapper));
    }

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

    @ApiOperation(value = "新增 - 手动查询当天赛事")
    @GetMapping("/today")
    public String today(){
        System.out.println("异步线程开始");
        taskService.getBasketballTournament(DateUtil.getDate(0));
        System.out.println("异步线程结束");
        return "success";
    }

    @ApiOperation(value = "新增 - 手动查询当天和明天赛事")
    @GetMapping("/today/all")
    public String todayAll(){
        System.out.println("异步线程开始");
        taskService.getTodayAllBasketball(DateUtil.getDate(0));
        taskService.getTodayAllBasketball(DateUtil.getDate(1));
        System.out.println("异步线程结束");
        return "success";
    }

    @ApiOperation(value = "新增 - 手动查询明天赛事")
    @GetMapping("/tomorrow")
    public String tomorrow(){
        System.out.println("异步线程开始");
        taskService.getBasketballTournament(DateUtil.getDate(1));
        System.out.println("异步线程结束");
        return "success";
    }

    @ApiOperation(value = "新增 - 手动查询昨天赛事")
    @GetMapping("/yesterday")
    public String yesterday(){
        System.out.println("异步线程开始");
        taskService.getBasketballTournament(DateUtil.getDate(-1));
        System.out.println("异步线程结束");
        return "success";
    }


    @ApiOperation(value = "编辑 - 手动查询当天结束赛事")
    @GetMapping("/today/end")
    public String todayEnd(){
        System.out.println("异步线程开始");
        taskService.getEndBasketballTournament(DateUtil.getDate(0));
        System.out.println("异步线程结束");
        return "success";
    }

    @ApiOperation(value = "编辑 - 手动查询昨天结束赛事")
    @GetMapping("/yesterday/end")
    public String yesterdayEnd(){
        System.out.println("异步线程开始");
        taskService.getEndBasketballTournament(DateUtil.getDate(-1));
        System.out.println("异步线程结束");
        return "success";
    }

    @ApiOperation(value = "编辑 - 手动更新今天天结束赛事竞猜结果")
    @GetMapping("/today/quiz")
    public String todayEndQuiz(){
        System.out.println("异步线程开始");
        taskService.UpdateQuizResult(DateUtil.getDate(0));
        System.out.println("异步线程结束");
        return "success";
    }

    @ApiOperation(value = "编辑 - 手动更新昨天结束赛事竞猜结果")
    @GetMapping("/yesterday/end/quiz")
    public String yesterdayEndQuiz(){
        System.out.println("异步线程开始");
        taskService.UpdateQuizResult(DateUtil.getDate(-1));
        System.out.println("异步线程结束");
        return "success";
    }

}

