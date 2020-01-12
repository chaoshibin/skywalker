package com.github.skywalker.distribute.lock.service.impl;


import com.github.skywalker.distribute.lock.LockEnum;
import com.github.skywalker.distribute.lock.service.DistributeLockService;
import com.github.skywalker.redis.RedisUtils;

/**
 * @author CHAO 2019/5/16 17:20
 */
public class RedisDistributeLockServiceImpl implements DistributeLockService {
    @Override
    public boolean lock(String lockKey, String requestId, long expireSeconds) {
        return RedisUtils.lock(lockKey, requestId, expireSeconds);
    }

    @Override
    public boolean unLock(String lockKey, String requestId) {
        return RedisUtils.unlock(lockKey, requestId);
    }

    @Override
    public LockEnum type() {
        return LockEnum.REDIS;
    }
}