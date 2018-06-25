package com.example.demo.lock.remote;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIServerSocketFactory;

/**
 * get rmi  way
 */
public class RmiDistributeLockFactory {

    private static final Log logger = LogFactory.getLog(RmiDistributeLockFactory.class);

    private static final String HOST = "localhost";
    private static final int PORT = 1099;

    /**
     * host
     */
    private String host = HOST;
    /**
     * port
     */
    private int port = PORT;

    private RmiDistributeLockFactory() {
    }

    public static RmiDistributeLockFactory of(String host, int port) {
        RmiDistributeLockFactory factory = new RmiDistributeLockFactory();
        factory.host = host;
        factory.port = port;
        return factory;
    }

    public static synchronized RmiDistributeLockFactory ofLocal() throws RemoteException {
        RmiDistributeLockFactory factory = new RmiDistributeLockFactory();
        return factory;
    }

    public DistributedLock get(String key) throws RemoteException, NamingException, NotBoundException {
        String sync = "FactorySyncGet";
        synchronized (sync) {
            Registry registry = LocateRegistry.getRegistry(this.host, this.port);
            if (registry == null) {
                logger.error(this.host + ":" + this.port + "can  not connect");
                throw new RuntimeException();
            }

            DistributedLock lock = null;

            try {
                lock = (DistributedLock) registry.lookup(key);
            } catch (NotBoundException e) {
                lock = new RmiDistributedLock();
                registry.rebind(key, lock);
            }
            if (lock == null) {

            } else {
                return lock;
            }
            return (RmiDistributedLock) registry.lookup(key);
        }
    }

}
