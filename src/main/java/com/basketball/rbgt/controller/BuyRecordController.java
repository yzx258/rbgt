package com.basketball.rbgt.controller;

import com.basketball.rbgt.config.Response;
import com.basketball.rbgt.pojo.spec.BuyRecordSpec;
import com.basketball.rbgt.service.BuyRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @company： 厦门宜车时代信息技术有限公司
 * @copyright： Copyright (C), 2020
 * @author： yucw
 * @date： 2020/10/16 14:17
 * @version： 1.0
 * @description:
 */
@Api(value = "/buy",tags = "篮球赛事相关接口")
@CrossOrigin
@RestController
@RequestMapping("/buy")
public class BuyRecordController {

    @Autowired
    private BuyRecordService buyRecordService;

    @ApiOperation(value = "新增 - 篮球下注记录")
    @PostMapping("/add")
    public Response add(@RequestBody BuyRecordSpec spec){
        buyRecordService.add(spec);
        return new Response<>();
    }

    @ApiOperation(value = "查询 - 篮球黑单记录")
    @GetMapping("/get/{type}")
    public Response get(@PathVariable("type") String type){
        System.out.println(type);
        return new Response(buyRecordService.get(type));
    }

    @ApiOperation(value = "更新 - 篮球黑单记录")
    @GetMapping("/del/{type}")
    public Response del(@PathVariable("type") String type){
        System.out.println(type);
        buyRecordService.del(type);
        return new Response<>();
    }

}
