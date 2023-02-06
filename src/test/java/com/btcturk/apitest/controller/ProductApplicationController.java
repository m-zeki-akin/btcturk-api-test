package com.btcturk.apitest.controller;

import com.btcturk.apitest.infrastructure.user.User;
import lombok.Getter;

@Getter
public class ProductApplicationController extends BaseController {
    private final String baseUrl = "http://localhost:8089";

    public ProductApplicationController(User user) {
        super(user);
    }
}
