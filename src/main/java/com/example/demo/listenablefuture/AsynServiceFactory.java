package com.example.demo.listenablefuture;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public abstract class AsynServiceFactory<S> implements InvocationHandler {
    private S service;
    public AsynServiceFactory() {
    }
    public final S createAsynService() throws Exception {
        this.service = (S)this;
        InvocationHandler handler = this;
        S s = (S) Proxy.newProxyInstance(handler.getClass().getClassLoader(), service
                .getClass().getInterfaces(), handler);
        return s;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        S service = (S)this;
        new Thread(new Runnable() {
            S service = null;
            @Override
            public void run() {
                try {
                    System.err.println(Thread.currentThread().getName());
                    Object result = method.invoke(this.service, args);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            public Runnable setService(S service){
                this.service = service;
                return this;
            }
        }.setService(service)).start();
        return null;
    }
}
