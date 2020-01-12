package com.github.skywalker.distribute.id.impl;

import com.github.skywalker.distribute.id.IDProvider;

import java.util.UUID;

/**
 * <p>
 *
 * </p>
 *
 * @author CHAO
 * @since 2019/7/10
 **/
public class UUIDProvider implements IDProvider<String> {
    @Override
    public String generateKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}