package com.github.skywalker.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Chao Shibin 2019/4/16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EasyValidation {
    /**
     * 错误码域
     */
    String codeField() default "code";

    /**
     * 错误信息域
     */
    String msgField() default "msg";

    /**
     * 参数校验失败错误码
     */
    String code() default "999998";
}
