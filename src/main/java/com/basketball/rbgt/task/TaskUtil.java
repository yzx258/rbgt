package com.basketball.rbgt.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.basketball.rbgt.mapper.EventMapper;
import com.basketball.rbgt.mapper.ReportMapper;
import com.basketball.rbgt.pojo.Event;
import com.basketball.rbgt.pojo.Report;
import com.basketball.rbgt.util.DateUtil;
import com.basketball.rbgt.util.HtmlUtil;
import com.basketball.rbgt.util.QuizResultsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: rbgt
 * @author: 俞春旺
 * @create: 2020/06/14 09:03
 * @description： 描述：
 */
@Service
public class TaskUtil {

    @Autowired
    private HtmlUtil htmlUtil;
    @Autowired
    private QuizResultsUtil quizResultsUtil;
    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private ReportMapper reportMapper;
    /**
     * 常量
     */
    private static final String quiz = "红";

    /**
     * 描述：异步获取篮球赛事
     * @param ctime
     */
    @Async("myTaskAsyncPool")
    public void getBasketballTournament(String ctime)
    {
        // 获取篮球赛事
        htmlUtil.allEvent(ctime,true);
    }

    /**
     * 描述：异步获取单日全部篮球赛事
     * @param ctime
     */
    @Async("myTaskAsyncPool")
    public void getTodayAllBasketball(String ctime)
    {
        // 获取篮球赛事
        htmlUtil.insertAllEvent(ctime);
    }

    /**
     * 描述：异步获取篮球赛事
     * @param ctime
     */
    @Async("myTaskAsyncPool")
    public void getEndBasketballTournament(String ctime)
    {
        // 获取篮球赛事
        htmlUtil.allEvent(ctime,false);
    }

    /**
     * 描述：异步获取当天篮球赛事
     * @param ctime
     */
    @Async("myTaskAsyncPool")
    public void UpdateQuizResult(String ctime)
    {
        QueryWrapper<Event> queryWrapper = new QueryWrapper<Event>();
        queryWrapper.eq("start_time",ctime).eq("status",1);
        List<Event> events = eventMapper.selectList(queryWrapper);
        for (Event e : events){
            e.setResults(quizResultsUtil.getQuizResultsUtil(e));
            e.setStatus(2);
            eventMapper.updateById(e);
        }
    }

    /**
     * 描述：异步统计单月的篮球赛事
     */
    @Async("myTaskAsyncPool")
    public void UpdateByMonthQuizResult()
    {
        QueryWrapper<Event> queryWrapper = new QueryWrapper<Event>();
        queryWrapper.eq("month",DateUtil.getDate(0).split("-")[1]).eq("status",1).eq("status",1);
        List<Event> events = eventMapper.selectList(queryWrapper);
        for (Event e : events){
            e.setResults(quizResultsUtil.getQuizResultsUtil(e));
            e.setStatus(2);
            eventMapper.updateById(e);
        }
    }

    /**
     * 描述：添加下注指令
     */
    @Async("myTaskAsyncPool")
    public void addBet(String ctime)
    {
        htmlUtil.allBetEvent(ctime);
    }

    /**
     * 描述：异步获取报表信息

     */
    @Async("myTaskAsyncPool")
    public void updateReport()
    {
        String year = DateUtil.getDate(0).split("-")[0];
        String month = DateUtil.getDate(0).split("-")[1];
        // 删除单月已统计的数据
        QueryWrapper<Report> deleteWrapper = new QueryWrapper<Report>();
        deleteWrapper.eq("month",month);
        reportMapper.delete(deleteWrapper);
        // 查询当月数据总计
        QueryWrapper<Event> queryWrapper = new QueryWrapper<Event>();
        queryWrapper.eq("status", 2).eq("month",month);
        List<Event> events = eventMapper.selectList(queryWrapper);
        int red = events.stream().filter(e -> quiz.equals(e.getResults())).collect(Collectors.toList()).size();
        Report r = new Report();
        // 设置红单
        r.setAmount(red);
        // 设置创建时间
        r.setCreateTime(DateUtil.getDate(0));
        // 设置年月
        r.setYear(year);
        r.setMonth(month);
        r.setQuizType(1);
        reportMapper.insert(r);
        Report b = new Report();
        // 设置黑单 = 总单 - 红单
        b.setAmount(events.size() - red);
        // 设置创建时间
        b.setCreateTime(DateUtil.getDate(0));
        // 设置年月
        b.setYear(year);
        b.setMonth(month);
        b.setQuizType(0);
        reportMapper.insert(b);
    }
}