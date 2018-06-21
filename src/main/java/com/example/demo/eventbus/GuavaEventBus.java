package com.example.demo.eventbus;

import com.google.common.eventbus.*;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GuavaEventBus{
    private static AsyncEventBus asyncEventBus = new AsyncEventBus(Executors.newFixedThreadPool(10));
    public static void register(GuavaListener listener,GuavaListener ...guavaListeners){
        asyncEventBus.register(listener);
    }
    public static void post(GuavaEvent event){
        asyncEventBus.post(event);
    }
}
