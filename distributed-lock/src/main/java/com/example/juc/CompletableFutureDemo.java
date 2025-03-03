package com.example.juc;

import java.util.concurrent.*;

/**
 * ClassName: CompletableFutureDemo
 * Package: com.example.juc
 * Description:
 *
 * @Author Harizon
 * @Create 2025/3/2 16:18
 * @Version 1.0
 */
public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException,
            InterruptedException, TimeoutException {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println("-----come in FutureTask");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "" + ThreadLocalRandom.current().nextInt(100);
        });
        Thread t1 = new Thread(futureTask, "t1");
        t1.start();
        //3 秒钟后才出来结果，还没有计算你提前来拿(只要一调用 get 方法，对于结果就是不见不散，会导致阻塞 )
        //System.out.println(Thread.currentThread().getName() + "\t" + futureTask.get());
        //3 秒钟后才出来结果，我只想等待 1 秒钟，过时不候
        System.out.println(Thread.currentThread().getName() + "\t" + futureTask.get(1L, TimeUnit.SECONDS));
        System.out.println(Thread.currentThread().getName() + "\t" + " run... here");
    }
}
