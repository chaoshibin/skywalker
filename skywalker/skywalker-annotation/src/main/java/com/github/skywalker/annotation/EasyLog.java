package com.github.skywalker.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Chao Shibin 2019/4/16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EasyLog {
    @AliasFor("title")
    String value() default "";

    /**
     * 打印日志标识
     */
    @AliasFor("value")
    String title() default "";

    /**
     * 错误码域
     */
    String codeField() default "code";

    /**
     * 错误描述域
     */
    String msgField() default "msg";
}
