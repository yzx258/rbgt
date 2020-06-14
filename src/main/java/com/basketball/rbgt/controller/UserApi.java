package com.basketball.rbgt.controller;

import com.alibaba.fastjson.JSONObject;
import com.basketball.rbgt.Target.UserLoginToken;
import com.basketball.rbgt.config.Response;
import com.basketball.rbgt.mapper.UserMapper;
import com.basketball.rbgt.pojo.User;
import com.basketball.rbgt.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: rbgt
 * @author: 俞春旺
 * @create: 2020/06/12 21:41
 * @description： 描述：
 */
@Api(value = "/api",tags = "JWT测试")
@RestController
@RequestMapping("api")
public class UserApi {
    @Autowired
    UserMapper userService;
    @Autowired
    TokenService tokenService;

    @ApiOperation(value = "查询 - 登录")
    @PostMapping("/login")
    public Response login(@ApiParam(value = "user") @RequestBody User user){
        JSONObject jsonObject=new JSONObject();
        User userForBase= userService.selectById(user.getId());
        if(userForBase==null){
            jsonObject.put("message","登录失败,用户不存在");
            return new Response(jsonObject);
        }else {
            if (!userForBase.getPassword().equals(user.getPassword())){
                jsonObject.put("message","登录失败,密码错误");
                return new Response(jsonObject);
            }else {
                String token = tokenService.getToken(userForBase);
                jsonObject.put("token", token);
                jsonObject.put("user", userForBase);
                return new Response(jsonObject);
            }
        }
    }
}