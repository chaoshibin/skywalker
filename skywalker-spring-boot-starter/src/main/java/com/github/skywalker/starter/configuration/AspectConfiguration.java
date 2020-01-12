package com.github.skywalker.starter.configuration;

import com.github.skywalker.log.EasyLogAspect;
import com.github.skywalker.validation.EasyValidationAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author Chao Shibin 2019/5/18 18:09
 */
@Slf4j
public class AspectConfiguration {
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(EasyLogMarkerConfiguration.Marker.class)
    public EasyLogAspect easyLogAspect() {
        log.debug("初始化日志打印切面");
        return new EasyLogAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(EasyValidationMarkerConfiguration.Marker.class)
    public EasyValidationAspect easyValidationAspect() {
        log.debug("初始化参数校验切面");
        return new EasyValidationAspect();
    }
}
