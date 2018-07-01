package com.cglib.example;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsynServiceFactory<T> {
    public <T> T getAsynService() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.getClass());
        enhancer.setCallback(new AsynMethodIntercept());
        return (T) enhancer.create();

    }
}

class AsynMethodIntercept implements MethodInterceptor {
    final static ExecutorService pool = Executors.newFixedThreadPool(20);
    final static ListeningExecutorService service = MoreExecutors.listeningDecorator(pool);

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
        //System.err.println(o);


        try {

            service.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.err.println(Thread.currentThread().getName());
                        methodProxy.invokeSuper(o, objects);
                    } catch (Exception e) {
                        e.printStackTrace();

                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            service.shutdown();
        }
        return null;
    }
}
