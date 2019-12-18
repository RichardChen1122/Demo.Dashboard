package com.siemens.dashboard.rainnier;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;

import reactor.core.publisher.Mono;

public class RxDemo {
    public static void entry(String[] args) {
		displayCurrentTime(1);
		displayCurrentThreadId(1);
		Mono.just(10).delayElement(Duration.ofSeconds(5))
			.map(n->{
				displayCurrentTime(2);
				displayCurrentThreadId(2);
				displayValue(n);
				delaySeconds(2);
				return n+1;
			})
			.filter(k->{
				displayCurrentTime(3);
				displayCurrentThreadId(3);
				displayValue(k);
				delaySeconds(3);
				return k%2==0;
			})
			.defaultIfEmpty(9)
			.subscribe(t->{
				displayCurrentTime(4);
				displayCurrentThreadId(4);
				displayValue(t);
				delaySeconds(2);
				System.out.println(t + " consumed, worker Thread over, exit.");
			});
		displayCurrentTime(5);
		displayCurrentThreadId(5);	
		// System.out.println(a.block());
	}

	static void displayCurrentTime(int point){
		System.out.println(point+":" + LocalTime.now());
	}

	static void displayCurrentThreadId(int point){
		System.out.println(point+":" + Thread.currentThread().getId());
	}

	//显示当前的数值
	static void displayValue(int n) {
		System.out.println("input : " + n);
	}

	//延迟若干秒
	static void delaySeconds(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	//主线程暂停
	static void pause() {
		try {
			System.out.println("main Thread over, paused.");
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}