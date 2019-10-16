package com.github.skywalker.starter.annotation;

import com.github.skywalker.starter.configuration.EasyLogMarkerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Chao Shibin 2019/8/24 19:43
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EasyLogMarkerConfiguration.class)
public @interface EnableEasyLog {

}
