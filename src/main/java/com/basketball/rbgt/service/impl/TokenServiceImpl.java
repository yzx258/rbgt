package com.basketball.rbgt.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.basketball.rbgt.pojo.User;
import com.basketball.rbgt.service.TokenService;
import org.springframework.stereotype.Service;

/**
 * @program: rbgt
 * @author: 俞春旺
 * @create: 2020/06/12 21:45
 * @description： 描述：
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Override
    public String getToken(User user) {
        String token="";
        token= JWT.create().withAudience(user.getId()+"")
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }
}