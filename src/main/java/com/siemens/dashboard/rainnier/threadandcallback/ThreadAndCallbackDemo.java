package com.siemens.dashboard.rainnier.threadandcallback;

import java.util.concurrent.CountDownLatch;
import org.springframework.util.StopWatch;


public class ThreadAndCallbackDemo {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		blockingCall();
		ThreadAdnCallbackCall();
		CompletableFutureCall();
		publisherCall();
	}

	private static void blockingCall() {
		HomepageService homepageService = new HomepageService();
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		String userInfo = homepageService.getUserInfo();

		System.out.println(userInfo);
		System.out.println(homepageService.getNotice());
		System.out.println(homepageService.getTodos(userInfo));
		stopWatch.stop();
		System.out.println("call methods costs " + stopWatch.getTotalTimeMillis() + "mills");
	}

	private static void ThreadAdnCallbackCall() {
		CountDownLatch ct = new CountDownLatch(3);
		HomepageService homepageService = new HomepageService();
		HomePageServiceThreasAndCallbackWrapper homePageServiceFutureWrapper = new HomePageServiceThreasAndCallbackWrapper(
				homepageService);

		Runnable finallyCallback = () -> {
			ct.countDown();
		};

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		homePageServiceFutureWrapper.getUserInfoAsync((userInfo) -> {
			System.out.println(userInfo);
			homePageServiceFutureWrapper.getTodos(userInfo, (todos) -> {
				System.out.println(todos);
			}, System.err::println, finallyCallback);
		}, System.err::println, finallyCallback);
		homePageServiceFutureWrapper.getNoticeAsync(System.out::println, System.err::println, finallyCallback);

		try {
			ct.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		stopWatch.stop();

		System.out.println("thread and callback aysnc call methods costs " + stopWatch.getTotalTimeMillis() + "mills");
	}

	private static void CompletableFutureCall() throws InterruptedException, ExecutionException {
		CountDownLatch ct = new CountDownLatch(3);

		HomepageService homepageService = new HomepageService();

		HomepageServiceCompletableFutureWrapper homepageServiceCompletableFutureWrapper = new HomepageServiceCompletableFutureWrapper(homepageService);

		Runnable finallyCallback=()->{
			ct.countDown();
		};

		StopWatch stopWatch =new StopWatch();
		stopWatch.start();
		// homepageServiceCompletableFutureWrapper.getUserInfoAsync().thenCompose(
		// 	userinfo ->{
		// 		System.out.println(userinfo);
		// 		return homepageServiceCompletableFutureWrapper.getTodosAsync(userinfo);
		// 	}
		// ).thenAcceptAsync(System.out::println).thenRun(finallyCallback);
		homepageServiceCompletableFutureWrapper.getUserInfoAsync().thenAccept(System.out::println).thenRun(finallyCallback);
		homepageServiceCompletableFutureWrapper.getTodosAsync("user").thenAccept(System.out::println).thenRun(finallyCallback);
		homepageServiceCompletableFutureWrapper.getNoticeAsync().thenAccept(System.out::println).thenRun(finallyCallback);
		
		ct.await();
		stopWatch.stop();
		System.out.println("CompletableFuture async call methods costs " + stopWatch.getTotalTimeMillis() + " mills");
	}

	private static void publisherCall() throws InterruptedException, ExecutionException {
		CountDownLatch ct = new CountDownLatch(2);
        //统一的finallyCallback
        Runnable finallyCallback = () -> {
            ct.countDown();
        };
        StopWatch stopWatch = new StopWatch();
        HomepageService homePageService = new HomepageService();
        HomepageServicePublisherWrapper homePageServicePublisherWrapper =
				new HomepageServicePublisherWrapper(homePageService);
		homePageServicePublisherWrapper.getUserInfoAsync().doOnSubscribe(subscription->{
			stopWatch.start();
		}).doOnNext(System.out::println).doFinally(s -> finallyCallback.run()).subscribe();

		homePageServicePublisherWrapper
				.getTodosAsync("userinfo")
				.doOnNext(System.out::println)
				.doFinally((s) -> {
					finallyCallback.run();
				})
				.subscribe();

		homePageServicePublisherWrapper
                .getNoticeAsync()
                .doOnNext(System.out::println)
                .doFinally((s) -> {
                    finallyCallback.run();
                })
				.subscribe();
				
		ct.await();
		stopWatch.stop();

		System.out.println("Publisher async call methods costs " + stopWatch.getTotalTimeMillis() + " mills");
	}
}
