package com.github.skywalker.distribute.id;

/**
 * <p>
 * ID生成器
 * </p>
 *
 * @author CHAO
 * @since 2019/7/10
 **/
public interface IDProvider<T> {
    /**
     * 生成分布式key
     *
     * @return key
     */
    T generateKey();
}
