package com.example.demo;

import java.util.concurrent.TimeUnit;

public class EchoMethod{
    public static String echoAfterTime(String str, int delay, TimeUnit timeunit){
        try {
            timeunit.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return str;
    }
}