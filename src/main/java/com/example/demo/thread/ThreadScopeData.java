package com.example.demo.thread;

public class ThreadScopeData<T> {

    private T data;

    private ThreadScopeData() {
    }

    private static ThreadLocal<ThreadScopeData> map = new ThreadLocal<ThreadScopeData>();

    public static ThreadScopeData getThreadInstance() {
        ThreadScopeData instance = map.get();
        if (instance == null) {
            instance = new ThreadScopeData();
            map.set(instance);
        }
        return instance;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getDate() {
        return this.data;
    }
}
