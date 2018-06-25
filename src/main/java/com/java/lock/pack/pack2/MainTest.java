package com.java.lock.pack.pack2;


class T{
    synchronized void test() throws InterruptedException {
        synchronized ("") {
            Thread.sleep(3000);
            System.err.println("laji");
            synchronized (this){
                System.err.println("ercilaji");
            }
            synchronized (this.getClass()){
                while (1==1){

                }
            }
        }
    }
}

public class MainTest {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new T().test();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new T().test();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
