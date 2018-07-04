package com.java.lock.redis;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.util.Pool;

import java.util.Random;
import java.util.UUID;

public class RedisLockService {
    private static final Logger logger = LoggerFactory.getLogger(RedisLockService.class);
    protected final int lockTimeout =15000 ;
    ThreadLocal<UUID> threadLocal = new ThreadLocal<UUID>();
    RedisLockService(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }
    RedisLockService(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }


    protected boolean isNullOrEmpty(String string){
        return string == null || string.length() == 0;
    }
    public boolean lock(String lockKey) {
        UUID lockId = UUID.randomUUID();
        final String setResponse = getResource().set(lockKey, lockId.toString(), "NX", "PX", lockTimeout);
        boolean lockAcquired = !isNullOrEmpty(setResponse) && setResponse.equals("OK");
        if (lockAcquired) {
            // save the random value used to lock so that we can successfully unlock later
            threadLocal.set(lockId);
        }
        return lockAcquired;
    }
    protected int randomInt(final int min, final int max){
        return new Random().nextInt((max - min) + 1) + min;
    }
    public void waitForLock(String lockKey){
        while(!lock(lockKey)){
            try {
                logger.debug("Waiting for Redis lock.");
                Thread.sleep(randomInt(75, 125));
            } catch (InterruptedException e) {
                logger.error("Interrupted while waiting for lock.", e);
            }
        }
    }
    public boolean unlock(String lockKey){
        final String currentLock = getResource().get(lockKey);
        if(!isNullOrEmpty(currentLock) && UUID.fromString(currentLock).equals(threadLocal.get())){
            // This is our lock.  We can remove it.
            getResource().del(lockKey);
            return true;
        }
        return false;
    }
    private interface LockCallback<T> {
        T doWithLock(JedisCommands jedis) throws RuntimeException;
    }
    private JedisCluster jedisCluster;
    private Pool<Jedis> jedisPool;
    private JedisCommands getResource() throws RuntimeException {
        if (jedisCluster != null) {
            return jedisCluster;
        } else {
            return jedisPool.getResource();
        }
    }
    private <T> T doWithLock(LockCallback<T> callback, String lockKey) throws RuntimeException {
        JedisCommands jedis = null;
        try {
            jedis = getResource();
            try {
                this.waitForLock(lockKey);
                return callback.doWithLock(jedis);
            } catch (RuntimeException e) {
                throw e;
            } finally {
                this.unlock(lockKey);
            }
        } finally {
            if (jedis != null && jedis instanceof Jedis) {
                // only close if we're not using a JedisCluster instance
                ((Jedis) jedis).close();
            }
        }
    }

}
