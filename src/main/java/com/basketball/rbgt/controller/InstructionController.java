package com.basketball.rbgt.controller;


import com.basketball.rbgt.config.Response;
import com.basketball.rbgt.pojo.Event;
import com.basketball.rbgt.pojo.Instruction;
import com.basketball.rbgt.service.InstructionService;
import com.basketball.rbgt.util.HtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 俞春旺
 * @since 2020-07-28
 */
@Api(value = "/instruction",tags = "下注指令相关接口")
@CrossOrigin
@RestController
@RequestMapping("/instruction")
public class InstructionController {

    @Autowired
    private InstructionService instructionService;

    @ApiOperation(value = "查询 - 获取待下注指令")
    @GetMapping("/get")
    public Response<List<Instruction>> get(){
        return new Response(instructionService.getByStatus());
    }

    @ApiOperation(value = "查询 - 获取待下注指令")
    @GetMapping("/get/today")
    public Response<List<Instruction>> getToday(){
        return new Response(instructionService.getToday());
    }

    @ApiOperation(value = "更新 - 失败下注指令")
    @GetMapping("/update/error/{id}")
    public Response updateError(@PathVariable("id") String id){
        instructionService.updateBetError(id);
        return new Response();
    }

    @ApiOperation(value = "更新 - 成功下注指令")
    @GetMapping("/update/success/{id}")
    public Response updateSuccess(@PathVariable("id") String id){
        instructionService.updateBetSuccess(id);
        return new Response();
    }

}

