package com.example.demo.main.Asyn;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsynService {
    @Async
    public void test() {
        System.err.println(Thread.currentThread().getName());
        System.err.println("laji");
    }
}
