package com.basketball.rbgt.controller;

import com.basketball.rbgt.mapper.NrMapper;
import com.basketball.rbgt.pojo.Nr;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public String test(){
        String name = "休斯顿火箭";
        List<Nr> nrs = nrMapper.selectList(null);
        nrs.stream().forEach(r -> {
            if(r.getName().equals(name)){
                name.replace(r.getName(), r.getTarget());
                log.info("我是转换的数据 -> {},{}",r.getName(),name);
            }
        });
        return name;
    }

}
