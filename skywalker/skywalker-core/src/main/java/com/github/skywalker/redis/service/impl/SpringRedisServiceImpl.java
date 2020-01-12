package com.github.skywalker.redis.service.impl;

import com.github.skywalker.redis.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author CHAO 2019/5/20 23:52
 */
public class SpringRedisServiceImpl implements RedisService {
    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> valueOperations;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public <T> void set(String key, T value, long expireSeconds) {
        valueOperations.set(this.buildKey(key), value, expireSeconds, TimeUnit.SECONDS);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        return (T) valueOperations.get(this.buildKey(key));
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(this.buildKey(key));
    }

    @Override
    public String buildKey(String key) {
        return key;
    }
}
