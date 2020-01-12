package com.github.skywalker.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author CHAO 2019/5/14 12:34
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EasyCachePut {

    /**
     * 缓存名称，可看做缓存名称前缀，实际缓存的key还会加上项目名
     */
    String cacheName() default "";

    /**
     * 缓存唯一性
     */
    String[] key() default {};

    /**
     * 0表示不指定缓存时间，会生成30-60分钟的默认值
     */
    long expireSeconds() default 0L;

    /**
     * if false, 不对null值进行缓存
     */
    boolean cacheNull() default true;

    /**
     * 不受缓存干预策略影响但会过期(暂未实现)
     */
    //boolean alwaysCache() default false;

    /**
     * 私有缓存（暂未实现）
     */
    //boolean privateCache() default true;
}
