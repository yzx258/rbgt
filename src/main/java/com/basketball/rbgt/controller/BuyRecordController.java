package com.basketball.rbgt.controller;

import com.basketball.rbgt.config.Response;
import com.basketball.rbgt.pojo.spec.BuyRecordSpec;
import com.basketball.rbgt.service.BuyRecordService;
import io.swagger.annotations.Api;
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

    @PostMapping("/add")
    public Response add(@RequestBody BuyRecordSpec spec){
        buyRecordService.add(spec);
        return new Response<>();
    }

    @GetMapping("/get/{type}")
    public Response get(@PathVariable("type") String type){
        System.out.println(type);
        return new Response(buyRecordService.get(type));
    }

}
