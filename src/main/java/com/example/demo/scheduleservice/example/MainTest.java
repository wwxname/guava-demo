package com.example.demo.scheduleservice.example;

import com.example.demo.scheduleservice.SchduledService;

public class MainTest {

    public static void  main(String args[]){
        SchduledService schduledService = new SchduledService();
        schduledService.startAsync();
    }
}
