package com.example.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class HomePageServiceThreasAndCallbackWrapper{
    private final HomepageService homepageService;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors()
    );

    public HomePageServiceThreasAndCallbackWrapper(HomepageService homepageService) {
       this.homepageService = homepageService;
       Runtime.getRuntime().addShutdownHook(new Thread(()->threadPool.shutdown()));
    }

    void getUserInfoAsync(Consumer<String> sucessCallback, Consumer<Throwable> errorCallback, Runnable finallyCallback){
        threadPool.submit(()->{
            try {
                String userInfo = this.homepageService.getUserInfo();
                sucessCallback.accept(userInfo);
            } catch (Throwable e) {
                errorCallback.accept(e);
            }
            finally{
                finallyCallback.run();
            }
        });
    }

    void getNoticeAsync(Consumer<String> sucessCallback, Consumer<Throwable> errorCallback, Runnable finallyCallback) {
        threadPool.submit(() -> {
            try {
                String notice = this.homepageService.getNotice();
                sucessCallback.accept(notice);
            } catch (Throwable ex) {
                errorCallback.accept(ex);
            } finally {
                finallyCallback.run();
            }

        });
    }

    void getTodos(String userInfo, Consumer<String> sucessCallback, Consumer<Throwable> errorCallback, Runnable finallyCallback) {
        threadPool.submit(() -> {
            try {
                String todos = this.homepageService.getTodos(userInfo);
                sucessCallback.accept(todos);
            } catch (Throwable ex) {
                errorCallback.accept(ex);
            } finally {
                finallyCallback.run();
            }

        });
    }
}