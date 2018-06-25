package com.java.lock.pack.pack1;

import java.util.HashMap;
import java.util.Map;

class Call implements Runnable {
    public Map map;
    int i = 10000;
    Call(Map map) {
        this.map = map;
    }

    @Override
    public void run() {

        while (i > 0) {
            map.put(i, i);
            i--;
        }
    }
}

class Call1 implements Runnable {
    public Map map;
    int i = 10000;

    Call1(Map map) {
        this.map = map;
    }

    @Override
    public void run() {
        while (i > 0) {
            map.put(i, 1000 - i);
            i--;
        }
    }
}

public class MainTest {
    public static HashMap map = new HashMap();

    public static void main(String args[]) {
        Thread t1 = new Thread(new Call(map));
        Thread t2 = new Thread(new Call1(map));
        t1.start();
        t2.start();
        map.forEach((k,v)->{
            System.out.println(k+"----"+v);
        });
    }
}
