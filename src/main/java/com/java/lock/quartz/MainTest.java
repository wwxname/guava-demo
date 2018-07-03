package com.java.lock.quartz;

import org.quartz.impl.jdbcjobstore.LockException;

import java.sql.Connection;
import java.sql.SQLException;

class T1 extends Thread {
    PlusProcessExecuteSemaphoreSupport semaphore;

    T1(PlusProcessExecuteSemaphoreSupport semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            PlusProcessExecuteSemaphoreSupport.TransactionCallback callback = new PlusProcessExecuteSemaphoreSupport.TransactionCallback() {
                @Override
                public Object execute(Connection conn) throws RuntimeException {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.err.println("t1");
                    return null;
                }
            };
            semaphore.executeInLock("wwx1", callback);
            System.err.println("1000000");
        } catch (LockException e) {
            e.printStackTrace();
        }


    }
}

class T2 extends Thread {
    PlusProcessExecuteSemaphoreSupport semaphore;

    T2(PlusProcessExecuteSemaphoreSupport semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            PlusProcessExecuteSemaphoreSupport.TransactionCallback callback = new PlusProcessExecuteSemaphoreSupport.TransactionCallback() {
                @Override
                public Object execute(Connection conn) throws RuntimeException {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.err.println("t2");
                    return null;
                }
            };
            semaphore.executeInLock("wwx1", callback);

            System.err.println("1000");
        } catch (LockException e) {
            e.printStackTrace();
        }


    }
}

public class MainTest {
    public static void main(String args[]) throws SQLException, LockException, InterruptedException {
        JDBCUtils.getConnection();
        PlusProcessExecuteSemaphoreSupport support = new PlusProcessExecuteSemaphoreSupport();
        PlusStdRowLockSemaphore semaphore = new PlusStdRowLockSemaphore();
        semaphore.setExpandedSQL(PlusStdRowLockSemaphore.SELECT_FOR_LOCK);
        semaphore.setExpandedInsertSQL(PlusStdRowLockSemaphore.INSERT_LOCK);
        semaphore.setSchedName("key");
        support.setLockHandler(semaphore);


        new T1(support).start();
        Thread.sleep(1000);
        new T2(support).start();

    }
}
