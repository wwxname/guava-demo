package com.example.demo.main;

import com.example.demo.main.Asyn.AsynService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.URL;

public class TTest {
public static  void  main(String args[]){

    AnnotationConfigApplicationContext context  = new AnnotationConfigApplicationContext("com.example.demo.main.Asyn");
    AsynService service = (AsynService)context.getBean("asynService");
    Class clazzs[]=service.getClass().getInterfaces();
    for(Class clazz : clazzs){
        System.err.println(clazz.getSimpleName());
    }

    service.test();
    System.err.println(Thread.currentThread().getName());

}

}
