package com.example.demo;

import java.util.concurrent.TimeUnit;

public class HomepageService {
    public String getUserInfo() {
        return EchoMethod.echoAfterTime("get user info", 50, TimeUnit.MILLISECONDS);
    }

    public String getNotice() {
        return EchoMethod.echoAfterTime("get notices", 50, TimeUnit.MILLISECONDS);
    }

    public String getTodos(String userInfo) {
        return EchoMethod.echoAfterTime("get todos", 100, TimeUnit.MILLISECONDS);
    }
}