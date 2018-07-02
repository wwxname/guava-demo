package com.java.lock.quartz;

public interface Constants {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constants.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */


    String TABLE_LOCKS = "LOCKS";



    String COL_SCHEDULER_NAME = "SCHED_NAME";



    // TABLE_LOCKS columns names
    String COL_LOCK_NAME = "LOCK_NAME";


    // MISC CONSTANTS
    String DEFAULT_TABLE_PREFIX = "QRTZ_";


    /**
     * @deprecated Whether a trigger has misfired is no longer a state, but
     * rather now identified dynamically by whether the trigger's next fire
     * time is more than the misfire threshold time in the past.
     */
    String STATE_MISFIRED = "MISFIRED";





}
