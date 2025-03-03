package com.example.juc;

import java.util.concurrent.TimeUnit;
import
        java.util.concurrent.atomic.AtomicBoolean;

/**
 * @auther zzyy
 * @create 2020-05-26 23:24
 */
public class StopThreadDemo {
    private final static AtomicBoolean atomicBoolean = new AtomicBoolean(true);

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (atomicBoolean.get()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch
                (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("-----hello");
            }
        }, "t1");
        t1.start();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        atomicBoolean.set(false);
    }
}