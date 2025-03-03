package com.example.juc;

import java.util.concurrent.*;

/**
 * ClassName: CompleteFutureDemo2
 * Package: com.example.juc
 * Description:
 *
 * @Author Harizon
 * @Create 2025/3/2 16:35
 * @Version 1.0
 */
public class CompletableFutureDemo2 {
    public static void main(String[] args)
            throws ExecutionException,
            InterruptedException {
        FutureTask<String> futureTask = new
                FutureTask<>(() -> {
            System.out.println("-----come inFutureTask");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "" + ThreadLocalRandom.current().nextInt(100);
        });
        new Thread(futureTask, "t1").start();
        System.out.println(Thread.currentThread().getName() + "\t" + " 线程完成任务");
/**
 * 用于阻塞式获取结果 , 如果想要异步获取结
 果 , 通常都会以轮询的方式去获取结果
 */
        while (true) {
            if (futureTask.isDone()) {
                System.out.println(futureTask.
                        get());
                break;
            }
        }
    }


    public static void main01(String[] args)
            throws ExecutionException,
            InterruptedException {
        CompletableFuture<Void> future =
                CompletableFuture.runAsync(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t" + "-----come in");
// 暂停几秒钟线程
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch
                    (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("-----task isover");
                });
        System.out.println(future.get());
    }


    static class cfuture4 {
        public static void main(String[] args)
                throws Exception {
            CompletableFuture<Integer>
                    completableFuture =
                    CompletableFuture.supplyAsync(() -> {
                        System.out.println(Thread.currentThread().getName() + "\t" + "-----comein");
                        int result =ThreadLocalRandom.current().nextInt(10);
// 暂停几秒钟线程
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch
                        (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("----- 计算结束耗时 1 秒钟，result ： " + result);
                        if (result > 6) {
                            int age = 10 / 0;
                        }
                        return result;
                    }).whenComplete((v, e) -> {
                        if (e == null) {
                            System.out.println("-----result: " + v);
                        }
                    }).exceptionally(e -> {
                        System.out.println("-----exception:" + e.getCause() + "\t" + e.getMessage());
                        return -44;
                    });
// 主线程不要立刻结束，否则CompletableFuture 默认使用的线程池会立刻关闭 : 暂停 3 秒钟线程
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch
            (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
