package com.java.lock.quartz;



import java.sql.Connection;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PlusDBSemaphore implements Semaphore, Constants,
        StdJDBCConstants, TablePrefixAware{
    private final Logger log = LoggerFactory.getLogger(getClass());

    ThreadLocal<HashSet<String>> lockOwners = new ThreadLocal<HashSet<String>>();

    private String sql;
    private String insertSql;

    private String tablePrefix;

    private String schedName;

    private String expandedSQL;
    private String expandedInsertSQL;

    public PlusDBSemaphore(String tablePrefix, String schedName, String defaultSQL, String defaultInsertSQL) {
        this.tablePrefix = tablePrefix;
        this.schedName = schedName;
        setSQL(defaultSQL);
        setInsertSQL(defaultInsertSQL);
    }
    private String schedNameLiteral = null;
    protected String getSchedulerNameLiteral() {
        if(schedNameLiteral == null)
            schedNameLiteral = "'" + schedName + "'";
        return schedNameLiteral;
    }
    private void setExpandedSQL() {
        if (getTablePrefix() != null && getSchedName() != null && sql != null && insertSql != null) {
            expandedSQL = Util.rtp(this.sql, getTablePrefix(), getSchedulerNameLiteral());
            expandedInsertSQL = Util.rtp(this.insertSql, getTablePrefix(), getSchedulerNameLiteral());
        }
    }
    protected void setSQL(String sql) {
        if ((sql != null) && (sql.trim().length() != 0)) {
            this.sql = sql.trim();
        }

        setExpandedSQL();
    }

    protected void setInsertSQL(String insertSql) {
        if ((insertSql != null) && (insertSql.trim().length() != 0)) {
            this.insertSql = insertSql.trim();
        }

        setExpandedSQL();
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Interface.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    protected Logger getLog() {
        return log;
    }
    private HashSet<String> getThreadLocks() {
        HashSet<String> threadLocks = lockOwners.get();
        if (threadLocks == null) {
            threadLocks = new HashSet<String>();
            lockOwners.set(threadLocks);
        }
        return threadLocks;
    }
    public boolean isLockOwner(String lockName) {
        return getThreadLocks().contains(lockName);
    }
    protected abstract void executeSQL(Connection conn, String lockName, String theExpandedSQL, String theExpandedInsertSQL)
            throws LockException;

    @Override
    public boolean obtainLock(Connection conn, String lockName) throws LockException {

        if(log.isDebugEnabled()) {
            log.debug(
                    "Lock '" + lockName + "' is desired by: "
                            + Thread.currentThread().getName());
        }
        if (!isLockOwner(lockName)) {

            executeSQL(conn, lockName, expandedSQL, expandedInsertSQL);

            if(log.isDebugEnabled()) {
                log.debug(
                        "Lock '" + lockName + "' given to: "
                                + Thread.currentThread().getName());
            }
            getThreadLocks().add(lockName);

        } else if(log.isDebugEnabled()) {
            log.debug(
                    "Lock '" + lockName + "' Is already owned by: "
                            + Thread.currentThread().getName());
        }

        return true;
    }

    @Override
    public void releaseLock(String lockName) throws LockException {
        if (isLockOwner(lockName)) {
            if(getLog().isDebugEnabled()) {
                getLog().debug(
                        "Lock '" + lockName + "' returned by: "
                                + Thread.currentThread().getName());
            }
            getThreadLocks().remove(lockName);

        } else if (getLog().isDebugEnabled()) {
            getLog().warn(
                    "Lock '" + lockName + "' attempt to return by: "
                            + Thread.currentThread().getName()
                            + " -- but not owner!",
                    new Exception("stack-trace of wrongful returner"));
        }
    }

    @Override
    public boolean requiresConnection() {
        return true;
    }

    @Override
    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;

        setExpandedSQL();
    }

    @Override
    public void setSchedName(String schedName) {
        this.schedName = schedName;

        setExpandedSQL();
    }


    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getInsertSql() {
        return insertSql;
    }

    public void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public String getSchedName() {
        return schedName;
    }

    public String getExpandedSQL() {
        return expandedSQL;
    }

    public void setExpandedSQL(String expandedSQL) {
        this.expandedSQL = expandedSQL;
    }

    public String getExpandedInsertSQL() {
        return expandedInsertSQL;
    }

    public void setExpandedInsertSQL(String expandedInsertSQL) {
        this.expandedInsertSQL = expandedInsertSQL;
    }
}
