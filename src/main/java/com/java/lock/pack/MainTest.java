package com.java.lock.pack;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.*;

public class MainTest {
    Condition condition = null;
    public static void main(String args[]) {
        Lock lock = null;


        ReentrantLock reentrantLock = null;//直接  实现


        ReadWriteLock readWriteLock = null;
        ReentrantReadWriteLock reentrantReadWriteLock = null;

        StampedLock stampedLock = null;//自己玩的


        ConcurrentHashMap<String,String> map = null;




    }
}
