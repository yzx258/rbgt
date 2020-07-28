package com.basketball.rbgt.controller;


import com.alibaba.fastjson.JSON;
import com.basketball.rbgt.config.Response;
import com.basketball.rbgt.pojo.Ratio;
import com.basketball.rbgt.pojo.spec.RatioSpec;
import com.basketball.rbgt.service.RatioService;
import com.basketball.rbgt.task.TaskUtil;
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
 * @since 2020-07-28
 */
@Api(value = "/ratio",tags = "自动计算倍率")
@CrossOrigin
@RestController
@RequestMapping("/ratio")
public class RatioController {

    @Autowired
    private RatioService ratioService;

    @ApiOperation(value = "新增 - 倍率")
    @PostMapping("/save")
    public Response saveRatio(@ApiParam(value = "ratioSpec") @RequestBody RatioSpec ratioSpec){
        System.out.println(JSON.toJSONString(ratioSpec));
        return new Response(ratioService.calculateRatio(ratioSpec.getStartBetPrice(),ratioSpec.getBetRatio()));
    }

}

