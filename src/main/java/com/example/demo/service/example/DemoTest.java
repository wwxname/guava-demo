package com.example.demo.service.example;

import com.example.demo.service.ServiceContainer;
import com.example.demo.service.annotation.ServletScan;

@ServletScan(value = "com.example.demo.service.example.servlet")
public class DemoTest {
    public static  void  main(String args[]){
        ServiceContainer container =new ServiceContainer(8082);
        container.run(DemoTest.class);
    }
}
