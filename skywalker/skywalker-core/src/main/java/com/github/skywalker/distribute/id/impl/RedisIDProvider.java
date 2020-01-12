package com.github.skywalker.distribute.id.impl;

import com.github.skywalker.distribute.id.IDProvider;

/**
 * <p>
 * 使用redis自增
 * </p>
 *
 * @author CHAO
 * @since 2019/7/10
 **/
public class RedisIDProvider implements IDProvider<Long> {
    @Override
    public Long generateKey() {
        return 0L;
    }
}