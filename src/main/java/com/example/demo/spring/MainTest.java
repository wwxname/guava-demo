package com.example.demo.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainTest {
    public static void main(String args[]){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.example.demo.spring");


        context.getBean("demoService");
    }
}
