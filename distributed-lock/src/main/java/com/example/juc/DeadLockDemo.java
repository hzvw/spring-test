package com.example.juc;

import java.util.concurrent.TimeUnit;

/**
 * @auther zzyy
 * @create 2020-05-14 10:56
 */
public class DeadLockDemo {
    public static void main(String[] args) {
        final Object objectLockA = new                Object();
        final Object objectLockB = new
                Object();
        new Thread(() -> {
            synchronized (objectLockA) {
                System.out.println(Thread.currentThread().getName() + "\t" + " 自己持有 A ，希望获得B");
// 暂停几秒钟线程
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (objectLockB) {
                    System.out.println(Thread.currentThread().getName() + "\t" + "A------- 已经获得 得 B");
                }
            }
        }, "A").start();
        new Thread(() -> {
            synchronized (objectLockB) {
                System.out.println(Thread.currentThread().getName() + "\t" + " 自己持有 B ，希望获得A");
// 暂停几秒钟线程
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (objectLockA) {
                    System.out.println(Thread.
                            currentThread().getName() + "\t" + "B------- 已经获得 得 A");
                }
            }
        }, "B").start();
    }
}