package com.example.demo.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class LockFactory {

    private static ConcurrentHashMap<String,Lock> locks;

    public static  Lock getLock(String name) {
        synchronized (LockFactory.class){
            Lock lock = locks.get(name);
            if(lock!=null){
                return lock;
            }
            lock = new ReentrantLock();
            locks.put(name,lock);
            return lock;
        }
    }

    private LockFactory() {
    }
}
