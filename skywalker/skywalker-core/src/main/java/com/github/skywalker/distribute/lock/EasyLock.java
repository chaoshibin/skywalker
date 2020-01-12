package com.github.skywalker.distribute.lock;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author CHAO 2019/4/16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EasyLock {
    /**
     * 锁名称
     */
    String name();

    /**
     * 业务键
     */
    String[] key() default {};

    /**
     * 锁类型
     */
    LockEnum type() default LockEnum.REDIS;

    /**
     * 超时时间
     */
    long expireSeconds() default 30 * 60;

    /**
     * 错误码域
     */
    String codeField() default "code";

    /**
     * 错误信息域
     */
    String msgField() default "msg";

    /**
     * 争夺分布式锁失败错误码
     */
    String code() default "999999";
}
