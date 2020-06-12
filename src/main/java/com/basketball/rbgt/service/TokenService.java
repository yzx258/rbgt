package com.basketball.rbgt.service;

import com.basketball.rbgt.pojo.User;

public interface TokenService {
    public String getToken(User user);
}
