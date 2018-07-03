package com.java.lock.quartz;


import org.quartz.impl.jdbcjobstore.LockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;


public class PlusProcessExecuteSemaphoreSupport {
    private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

    protected Logger getLog() {
        return log;
    }

    private Semaphore lockHandler = null;


    private boolean setTxIsolationLevelSequential = false;
    private boolean dontSetAutoCommitFalse = false;

    public boolean isDontSetAutoCommitFalse() {
        return dontSetAutoCommitFalse;
    }

    public boolean isTxIsolationLevelSerializable() {
        return setTxIsolationLevelSequential;
    }

    protected Connection getNonManagedTXConnection() {
        return getConnection();
    }

    protected Connection getAttributeRestoringConnection(Connection conn) {
        return (Connection) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{Connection.class},
                new AttributeRestoringConnectionInvocationHandler(conn));
    }

    protected Connection getConnection() {
        Connection conn;
        try {
            conn = JDBCUtils.getConnection();
        } catch (SQLException sqle) {
            throw new RuntimeException(
                    "Failed to obtain DB connection from data source '"
                            + "': " + sqle.toString(), sqle);
        } catch (Throwable e) {
            throw new RuntimeException(
                    "Failed to obtain DB connection from data source '"
                            + "': " + e.toString(), e);
        }

        if (conn == null) {
            throw new RuntimeException(
                    "Could not get connection from DataSource '"
                            + "'");
        }

        // Protect connection attributes we might change.
        conn = getAttributeRestoringConnection(conn);

        // Set any connection connection attributes we are to override.
        try {
            if (!isDontSetAutoCommitFalse()) {
                conn.setAutoCommit(false);
            }

            if (isTxIsolationLevelSerializable()) {
                conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            }
        } catch (SQLException sqle) {
            getLog().warn("Failed to override connection auto commit/transaction isolation.", sqle);
        } catch (Throwable e) {
            try {
                conn.close();
            } catch (Throwable ignored) {
            }

            throw new RuntimeException(
                    "Failure setting up connection.", e);
        }
        return conn;
    }

    protected interface TransactionCallback<T> {
        T execute(Connection conn) throws RuntimeException;
    }

    protected interface TransactionValidator<T> {
        Boolean validate(Connection conn, T result) throws RuntimeException;
    }

    protected Object executeInLock(
            String lockName,
            TransactionCallback txCallback) throws LockException {
        return executeInNonManagedTXLock(lockName, txCallback, null);
    }

    protected Semaphore getLockHandler() {
        return lockHandler;
    }
    protected Semaphore setLockHandler(Semaphore semaphore) {
        return this.lockHandler = semaphore;
    }


    protected void commitConnection(Connection conn)
            throws RuntimeException {

        if (conn != null) {
            try {
                conn.commit();
            } catch (SQLException e) {
                throw new RuntimeException(
                        "Couldn't commit jdbc connection. " + e.getMessage(), e);
            }
        }
    }

    protected void rollbackConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                getLog().error(
                        "Couldn't rollback jdbc connection. " + e.getMessage(), e);
            }
        }
    }

    private volatile boolean shutdown = false;
    private long dbRetryInterval = 15000L; // 15 secs

    public long getDbRetryInterval() {
        return dbRetryInterval;
    }

    protected <T> T retryExecuteInNonManagedTXLock(String lockName, TransactionCallback<T> txCallback) throws LockException {
        for (int retry = 1; !shutdown; retry++) {
            try {
                return executeInNonManagedTXLock(lockName, txCallback, null);
            } catch (RuntimeException jpe) {

            }
            try {
                Thread.sleep(getDbRetryInterval()); // retry every N seconds (the db connection must be failed)
            } catch (InterruptedException e) {
                throw new IllegalStateException("Received interrupted exception", e);
            }
        }
        throw new IllegalStateException("JobStore is shutdown - aborting retry");
    }

    protected <T> T executeInNonManagedTXLock(
            String lockName,
            TransactionCallback<T> txCallback, final TransactionValidator<T> txValidator) throws RuntimeException, LockException {
        boolean transOwner = false;
        Connection conn = null;
        try {
            if (lockName != null) {
                // If we aren't using db locks, then delay getting DB connection
                // until after acquiring the lock since it isn't needed.
                if (getLockHandler().requiresConnection()) {
                    conn = getNonManagedTXConnection();
                }

                transOwner = getLockHandler().obtainLock(conn, lockName);
            }

            if (conn == null) {
                conn = getNonManagedTXConnection();
            }

            final T result = txCallback.execute(conn);
            try {
                commitConnection(conn);
            } catch (RuntimeException e) {
                rollbackConnection(conn);
                if (txValidator == null || !retryExecuteInNonManagedTXLock(lockName, new TransactionCallback<Boolean>() {
                    @Override
                    public Boolean execute(Connection conn) throws RuntimeException {
                        return txValidator.validate(conn, result);
                    }
                })) {
                    throw e;
                }
            }


            return result;
        } catch (RuntimeException e) {
            rollbackConnection(conn);
            throw e;
        } finally {
            try {
                releaseLock(lockName, transOwner);
            } finally {
                cleanupConnection(conn);
            }
        }
    }

    protected void releaseLock(String lockName, boolean doIt) {
        if (doIt) {
            try {
                getLockHandler().releaseLock(lockName);
            } catch (LockException le) {
                getLog().error("Error returning lock: " + le.getMessage(), le);
            }
        }
    }

    protected void cleanupConnection(Connection conn) {
        if (conn != null) {
            if (conn instanceof Proxy) {
                Proxy connProxy = (Proxy) conn;

                InvocationHandler invocationHandler =
                        Proxy.getInvocationHandler(connProxy);
                if (invocationHandler instanceof AttributeRestoringConnectionInvocationHandler) {
                    AttributeRestoringConnectionInvocationHandler connHandler =
                            (AttributeRestoringConnectionInvocationHandler) invocationHandler;

                    connHandler.restoreOriginalAtributes();
                    closeConnection(connHandler.getWrappedConnection());
                    return;
                }
            }

            // Wan't a Proxy, or was a Proxy, but wasn't ours.
            closeConnection(conn);
        }
    }

    protected void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                getLog().error("Failed to close Connection", e);
            } catch (Throwable e) {
                getLog().error(
                        "Unexpected exception closing Connection." +
                                "  This is often due to a Connection being returned after or during shutdown.", e);
            }
        }
    }

}
