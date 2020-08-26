package com.basketball.rbgt.controller;


import com.basketball.rbgt.pojo.Event;
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
    private HtmlUtil htmlUtil;

    @ApiOperation(value = "编辑 - 手动更新单月结束赛事竞猜结果")
    @PostMapping("/test")
    public String test(@RequestBody List<Event> eventList){
        htmlUtil.allBetEvent(eventList);
        return "success";
    }

}

