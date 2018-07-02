package com.java.lock.quartz;

public interface TablePrefixAware {
    void setTablePrefix(String tablePrefix);
    void setSchedName(String schedName);
}
