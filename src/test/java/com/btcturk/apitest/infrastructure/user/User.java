package com.btcturk.apitest.infrastructure.user;

import lombok.*;

@Getter
public class User {
    private final Integer id;
    private final String username;
    private final String password;
    private final String accessToken;
    @Setter
    private Boolean isBusy = false;

    public User(
            Integer id,
            String username,
            String password,
            String accessToken
    ){
        this.id = id;
        this.username = username;
        this.password = password;
        this.accessToken = accessToken;
    }

}
