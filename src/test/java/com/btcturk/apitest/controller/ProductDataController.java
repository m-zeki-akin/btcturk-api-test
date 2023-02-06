package com.btcturk.apitest.controller;

import com.btcturk.apitest.infrastructure.user.User;
import lombok.Getter;

@Getter
public class ProductDataController extends BaseController{
    private final String baseUrl = "https://gist.githubusercontent.com/sinanerdinc/71d6a6442d805ce0bc33a6feb2e2c6b5/raw/750f7305a931b7a2577c805a7e3cde12bee33124";

    public ProductDataController(User user){
        super(user);
    }
}
