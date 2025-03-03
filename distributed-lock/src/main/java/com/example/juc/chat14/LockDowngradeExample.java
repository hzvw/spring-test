package com.example.juc.chat14;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockDowngradeExample {
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();
    private final ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();

    private int sharedData = 0;

    public void downgradeLock() {
        writeLock.lock(); // 获取写锁
        try {
            // 修改共享资源
            sharedData++;
            System.out.println("写入数据: " + sharedData);

            // 锁降级：获取读锁，然后释放写锁
            readLock.lock();
        } finally {
            writeLock.unlock(); // 释放写锁
        }

        try {
            // 以读锁的形式持有锁，允许其他线程读取
            System.out.println("读取数据（降级后）: " + sharedData);
        } finally {
            readLock.unlock(); // 释放读锁
        }
    }

    public static void main(String[] args) {
        LockDowngradeExample example = new LockDowngradeExample();
        example.downgradeLock();
    }
}