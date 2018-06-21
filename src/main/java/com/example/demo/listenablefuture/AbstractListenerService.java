package com.example.demo.listenablefuture;

import com.google.common.util.concurrent.*;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * guava  简单封装  易用为主
 * @param <V>
 */
public abstract class AbstractListenerService<V> implements ListenerService<V> {
    class CallableWapper<V> implements Callable<V> {
        private AbstractListenerService<V> listenerService;

        public CallableWapper(AbstractListenerService abstractListenerService) {
            this.listenerService = abstractListenerService;
        }

        @Override
        public V call() throws Exception {
            return listenerService.call();
        }
    }

    class FutureCallbackWapper<V> implements FutureCallback<V> {
        private AbstractListenerService<V> listenerService;

        public FutureCallbackWapper(AbstractListenerService listenerService) {
            this.listenerService = listenerService;
        }

        @Override
        public void onSuccess(V result) {
            listenerService.onSuccess(result);
        }
        @Override
        public void onFailure(Throwable t) {
            listenerService.onFailure(t);
        }
    }

    private ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

    public final void excute() {
        ListenableFuture future = service.submit(new CallableWapper<V>(this));
        Futures.addCallback(future, new FutureCallbackWapper<V>(this), MoreExecutors.directExecutor());
    }


}
