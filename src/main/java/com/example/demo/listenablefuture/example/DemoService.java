package com.example.demo.listenablefuture.example;

import com.example.demo.listenablefuture.AbstractListenerService;

public class DemoService extends AbstractListenerService<Integer> {


    @Override
    public Integer call() throws Exception {
        throw  new RuntimeException();
//        /return 1;
    }

    @Override
    public void onSuccess(Integer result) {
        System.err.println("niubi");
    }

    @Override
    public void onFailure(Throwable thrown) {
        System.err.println("laji");
    }
    public  static void  main(String args[]){
        new DemoService().excute();

    }
}
