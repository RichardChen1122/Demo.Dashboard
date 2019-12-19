package com.example.demo;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class HomepageServicePublisherWrapper{
    private final HomepageService homepageService;

    private Scheduler executor = Schedulers.elastic();

    public HomepageServicePublisherWrapper(HomepageService homepageService) {
        this.homepageService = homepageService;
    }

    public Mono<String> getUserInfoAsync(){
        return Mono.fromCallable(this.homepageService::getUserInfo)
                    .subscribeOn(this.executor);
    }

    public Mono<String> getNoticeAsync() {
        return Mono
                .fromCallable(this.homepageService::getNotice)
                .subscribeOn(this.executor);
    }

    public Mono<String> getTodosAsync(String userInfo) {
        return Mono
                .fromSupplier(() -> this.homepageService.getTodos(userInfo))
                .subscribeOn(this.executor);
    }
}