package com.basketball.rbgt.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.basketball.rbgt.config.Response;
import com.basketball.rbgt.mapper.NrMapper;
import com.basketball.rbgt.pojo.Nr;
import com.basketball.rbgt.pojo.spec.NrSpec;
import com.basketball.rbgt.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @company： 厦门宜车时代信息技术有限公司
 * @copyright： Copyright (C), 2020
 * @author： yucw
 * @date： 2020/9/10 11:32
 * @version： 1.0
 * @description:
 */
@Api(value = "/replace",tags = "名称转换")
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/replace")
public class ReplaceCuntroller {

    @Autowired
    private NrMapper nrMapper;

    @ApiOperation(value = "查询 - 获取名称")
    @GetMapping("/test")
    public Response<String> test(){
        String name = "休斯顿火箭";
        List<Nr> nrs = nrMapper.selectList(null);
        for(Nr nr : nrs){
            if(nr.getName().equals(name)){
                name = name.replace(nr.getName(), nr.getTarget());
                log.info("我是转换的数据 -> {},{}",nr.getName(),name);
            }
        }
        return new Response<>(name);
    }

    @ApiOperation(value = "查询 - 获取名称")
    @GetMapping("/list")
    public Response<List<Nr>> getList(){
        QueryWrapper<Nr> qw = new QueryWrapper<Nr>();
        qw.orderByDesc("time");
        return new Response<>(nrMapper.selectList(qw));
    }

    @ApiOperation(value = "新增 - 需要替换的名称")
    @PostMapping(value = "/add")
    public Response<Nr> add(@RequestBody NrSpec nrSpec){
        Nr nr = new Nr();
        BeanUtil.copyProperties(nrSpec,nr,true);
        nr.setId(UUID.randomUUID().toString().replace("-",""));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date())+"";
        nr.setTime(time);
        nrMapper.insert(nr);
        return new Response<>(nr);
    }
}
