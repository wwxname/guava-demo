package com.example.demo.listenablefuture.example;

public class ATest {
    public static  void main(String args[]) throws Exception {
       DemoAsynServiceInter service =   new DemoAsynService().createAsynService();
       service.test();

    }
}
