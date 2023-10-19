package com.moensun.spring.boot.feign;

import feign.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "feign.logger", value = "enable", havingValue = "true")
public class LoggerConfiguration {

    @Bean
    Logger.Level feignLevel() {
        return Logger.Level.FULL;
    }
}
