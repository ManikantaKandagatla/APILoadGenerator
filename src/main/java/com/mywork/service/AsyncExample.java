package com.mywork.service;

import java.util.concurrent.*;

public class AsyncExample {
    public static void main(String[] args) {
        AsyncExample ex = new AsyncExample();
        System.out.println("In main before cf");
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(new Runnable() {
            @Override
            public void run() {
                ex.asyncMethod();
            }
        });
        System.out.println("In main method after cf");
        es.shutdown();
    }

    public void asyncMethod() {
        for(int i = 0 ;i < 5;i++) {
            System.out.println("In async method");
        }
    }
}
