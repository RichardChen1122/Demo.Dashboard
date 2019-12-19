package com.example.demo;

import java.util.concurrent.CompletableFuture;

public class HomepageServiceCompletableFutureWrapper{
    private final HomepageService homepageService;

    public HomepageServiceCompletableFutureWrapper(HomepageService homepageService) {
        this.homepageService = homepageService;
    }
    
    CompletableFuture<String> getUserInfoAsync(){
        return CompletableFuture.supplyAsync(this.homepageService::getUserInfo);
    }

    CompletableFuture<String> getNoticeAsync() {
        return CompletableFuture.supplyAsync(this.homepageService::getNotice);
    }
    //Same with above
    CompletableFuture<String> getTodosAsync(String userInfo) {
        return CompletableFuture.supplyAsync(() -> this.homepageService.getTodos(userInfo));
    }
}