package com.java.lock;

import java.rmi.server.RemoteObject;

class T {
    int i = 10000;

    public synchronized void test() throws InterruptedException {
        if (i > 0) {
            //Thread.sleep(0b1111101000);
            System.out.println(Thread.currentThread().getName() + "----" + i);
            i--;
        }
    }
}

class Call implements Runnable {
    public T t;

    @Override
    public void run() {
        synchronized ("") {
            while (1 > 0) {
                try {
                    t.test();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public Call setT(T t) {
        this.t = t;
        return this;
    }
}

public class MainTest {
    public static void main(String args[]) {

        RemoteObject r;

        Call call = new Call().setT(new T());
        Thread t1 = new Thread(call);
        Thread t2 = new Thread(call);
        t1.start();
        t2.start();
    }
}
