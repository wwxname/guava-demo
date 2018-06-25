package com.example.demo.lock.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public interface DistributedLock extends Remote {


    long lock() throws RemoteException, TimeoutException;

    long tryLock(long time, TimeUnit unit) throws RemoteException, TimeoutException;

    void unlock(long token) throws RemoteException;
    public long getToken() throws RemoteException;
}
