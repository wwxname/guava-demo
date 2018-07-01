package com.cglib.example;

public class MainTest {

    public static  void main(String args[]){
       DemoService demoService =   new DemoService().getAsynService();
       demoService.test();
        demoService.test();

    }
}
