package com.example.juc.chat5;

/**
 * ClassName: LockSupportDemo
 * Package: com.example.juc.chat5
 * Description:
 *
 * @Author Harizon
 * @Create 2025/3/2 17:31
 * @Version 1.0
 */
public class LockSupportDemo {
    public static void main(String[] args) {
        Object obj = new Object();

        new Thread(()->{
            synchronized (obj){
                try{
                    obj.wait();
                }catch(Exception e){

                }

            }


        }, "t1").start();


    }
}
