package com.basketball.rbgt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.basketball.rbgt.config.Response;
import com.basketball.rbgt.mapper.ReportMapper;
import com.basketball.rbgt.pojo.Report;
import com.basketball.rbgt.pojo.dto.ReportDto;
import com.basketball.rbgt.task.TaskUtil;
import com.basketball.rbgt.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 俞春旺
 * @since 2020-07-03
 */
@Api(value = "/report",tags = "历史报表")
@CrossOrigin
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private TaskUtil taskService;

    private static final String quiz = "红";

    @ApiOperation(value = "查询 - 当年历史报表数据")
    @GetMapping("/get/year")
    public Response yearReport(){
        QueryWrapper<Report> queryWrapper = new QueryWrapper<Report>();
        queryWrapper.eq("year", DateUtil.getDate(0).split("-")[0]);
        List<Report> reports = reportMapper.selectList(queryWrapper);
        return new Response(reports);
    }

    @ApiOperation(value = "查询 - 单月历史报表数据")
    @GetMapping("/get/month")
    public Response monthReport(){
        QueryWrapper<Report> queryWrapper = new QueryWrapper<Report>();
        queryWrapper.eq("month", DateUtil.getDate(0).split("-")[1]);
        List<Report> reports = reportMapper.selectList(queryWrapper);
        ReportDto r = new ReportDto();
        // 设置总数
        Integer allAmount = reports.get(0).getAmount()+reports.get(1).getAmount();
        r.setAllAmount(allAmount);
        // 设置年月
        r.setYear(DateUtil.getDate(0).split("-")[0]);
        r.setMonth(DateUtil.getDate(0).split("-")[1]);
        // 设置红单数
        r.setRedAmount(reports.get(0).getAmount());
        // 设置黑单数
        r.setBlackAmount(reports.get(1).getAmount());
        // 设置百分比
        r.setRedRatio(bs(r.getRedAmount(),allAmount)+"%");
        r.setBlackRatio(bs(r.getBlackAmount(),allAmount)+"%");
        return new Response(r);
    }

    /**
     * 描述：计算数据
     * @param a
     * @param b
     * @return
     */
    public int bs(int a ,int b){
        return (int)((new BigDecimal((float) a / b).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())*100);
    }

    @ApiOperation(value = "编辑 - 手动添加单月历史报表数据")
    @GetMapping("/update/month")
    public Response updateReport(){
        System.out.println("异步线程开始");
        taskService.updateReport();
        System.out.println("异步线程结束");
        return new Response("success");
    }
}

