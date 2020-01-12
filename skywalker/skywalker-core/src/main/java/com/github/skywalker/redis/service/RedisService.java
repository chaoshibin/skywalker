package com.github.skywalker.redis.service;

/**
 * @author CHAO 2019/5/20 23:50
 */
public interface RedisService {

    /**
     * set value
     *
     * @param key           key
     * @param value         value
     * @param expireSeconds 过期时间
     * @param <T>           value类型
     */
    <T> void set(String key, T value, long expireSeconds);

    /**
     * get value
     *
     * @param key   key
     * @param clazz 类
     * @param <T>   类型
     * @return value
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * delete value
     *
     * @param key key
     */
    void delete(String key);

    /**
     * 构建key
     *
     * @param key 业务键值
     * @return 实际key值
     */
    String buildKey(String key);
}
