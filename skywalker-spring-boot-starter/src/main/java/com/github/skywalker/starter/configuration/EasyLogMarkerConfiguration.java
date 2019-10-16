package com.github.skywalker.starter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chao Shibin 2019/8/24 19:52
 */
@Configuration
public class EasyLogMarkerConfiguration {
    @Bean
    public Marker easyLogMarkerBean() {
        return new Marker();
    }

    class Marker {
    }
}
