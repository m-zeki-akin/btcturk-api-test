package com.btcturk.apitest.controller;

import com.btcturk.apitest.infrastructure.user.User;
import lombok.Getter;

@Getter
public class BaseController {
    private final User user;

    public BaseController(User user){
        this.user = user;
    }
}
