package com.java.lock.quartz;

public class LockException extends RuntimeException{
    private static final long serialVersionUID = 3993800462589137228L;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructors.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public LockException(String msg) {
        super(msg);
    }

    public LockException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
