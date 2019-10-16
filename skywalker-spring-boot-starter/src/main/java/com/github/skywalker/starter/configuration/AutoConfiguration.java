package com.github.skywalker.starter.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Chao Shibin 2019/5/18 14:15
 */
@Configuration
@Import({AspectConfiguration.class})
public class AutoConfiguration {

}
