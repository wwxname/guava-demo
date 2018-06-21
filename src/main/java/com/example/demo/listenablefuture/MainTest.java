package com.example.demo.listenablefuture;

import com.google.common.base.Function;
import com.google.common.util.concurrent.*;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class MainTest {
    public static void main(String args[]) throws Exception {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture future1 = service.submit(new Callable<Integer>() {
            public Integer call() throws InterruptedException {
                Thread.sleep(1000);
                System.out.println("call future 1.");
                System.err.println(Thread.currentThread().getName());
                return 1;
            }
        });

        ListenableFuture future2 = service.submit(new Callable<Integer>() {
            public Integer call() throws InterruptedException {
                Thread.sleep(4000);
                System.out.println("call future 2.");
                System.err.println(Thread.currentThread().getName());


                return 2;
            }
        });


        final ListenableFuture<List<Integer>> allFutures = Futures.allAsList(future1, future2);

        final ListenableFuture<Integer> transform = Futures.transform(allFutures, new Function<List<Integer>, Integer>() {
            @Override
            public Integer apply(List<Integer> input) {
                return input.size();
            }
        }, MoreExecutors.directExecutor());

        Futures.addCallback(transform, new FutureCallback<Object>() {

            public void onSuccess(Object result) {
                System.out.println(result.getClass());
                System.out.printf("success with: %s%n", result);
            }

            public void onFailure(Throwable thrown) {
                System.out.printf("onFailure%s%n", thrown.getMessage());
                service.shutdown();
            }
        }, MoreExecutors.directExecutor());
        System.err.println(Thread.currentThread().getName() + "laji");
        System.out.println(transform.get());

        service.shutdown();

    }
}
