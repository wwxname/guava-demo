package com.example.demo.lock.remote.example;

import com.example.demo.lock.remote.DistributedLock;
import com.example.demo.lock.remote.RmiDistributeLockFactory;
import com.example.demo.lock.remote.RmiDistributedLock;
import com.sun.org.apache.xml.internal.security.signature.reference.ReferenceNodeSetData;
import sun.management.jmxremote.LocalRMIServerSocketFactory;

import javax.naming.NamingException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.TimeoutException;

//try {
//        RmiDistributedLock lock =  RmiDistributeLockFactory.ofLocalAuto().get("call");
//
//
//
//        } catch (RemoteException e) {
//        e.printStackTrace();
//        } catch (NamingException e) {
//        e.printStackTrace();
//        }
class Call implements Runnable {

    @Override
    public void run() {

        int i = 1000;
        DistributedLock lock = null;
        try {
            lock = RmiDistributeLockFactory.ofLocal().get("call");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        try {
            lock.lock();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        while (i > 0) {
            System.out.println(Thread.currentThread().getName()+"-----"+i);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i--;
        }
        try {
            lock.unlock(   lock.getToken());
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }
}

class Call_1 implements Runnable {

    @Override
    public void run() {
        int i = 1000;
        DistributedLock lock = null;
        try {
            lock = RmiDistributeLockFactory.ofLocal().get("call");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        try {
            lock.lock();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        while (i > 0) {

            System.out.println(Thread.currentThread().getName()+"-----"+i);
            i--;
        }
        try {
            lock.unlock(   lock.getToken());
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }
}

public class MainTest {
    public static void main(String args[]) throws RemoteException {
        LocateRegistry.createRegistry(1099);

        new Thread(new Call()).start();

        new Thread(new Call_1()).start();
    }

}
