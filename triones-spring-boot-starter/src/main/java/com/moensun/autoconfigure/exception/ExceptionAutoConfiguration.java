package com.moensun.autoconfigure.exception;

import com.moensun.commons.exception.spring.resource.ExceptionMessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration(value = "com.moensun.autoconfigure.exception.exceptionAutoConfiguration")
@EnableConfigurationProperties(
        value = {
                ExceptionProperties.class
        }
)
public class ExceptionAutoConfiguration {
    private final ExceptionProperties exceptionProperties;

    @Bean
    @ConditionalOnMissingBean
    public ExceptionMessageHandler exceptionMessageHandler() {
        ExceptionMessageHandler messageHandler = new ExceptionMessageHandler();
        messageHandler.setResourcePaths(exceptionProperties.getResourcePaths());
        return messageHandler;
    }
}
