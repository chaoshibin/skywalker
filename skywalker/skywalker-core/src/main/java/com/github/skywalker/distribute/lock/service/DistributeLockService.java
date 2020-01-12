package com.github.skywalker.distribute.lock.service;


import com.github.skywalker.distribute.lock.LockEnum;

/**
 * @author CHAO 2019/5/16 16:05
 */
public interface DistributeLockService {


    /**
     * 获取锁
     *
     * @param lockKey       锁标识
     * @param requestId     请求锁唯一ID用于实现可重入锁
     * @param expireSeconds 过期时间秒
     * @return 结果
     */
    boolean lock(String lockKey, String requestId, long expireSeconds);

    /**
     * 释放锁
     *
     * @param lockKey   锁标识
     * @param requestId 请求锁唯一ID用于实现可重入锁
     * @return 是否成功获取锁
     */
    boolean unLock(String lockKey, String requestId);

    /**
     * 锁类型
     *
     * @return LockEnum
     */
    LockEnum type();
}
