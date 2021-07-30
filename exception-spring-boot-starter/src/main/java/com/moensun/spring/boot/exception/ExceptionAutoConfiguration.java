package com.moensun.spring.boot.exception;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.moensun.commons.exception.spring.resource.ExceptionMessageHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
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
        messageHandler.setResourceBundle(exceptionProperties.getResourcePath());
        messageHandler.setResourceBundles(exceptionProperties.getResourcePaths());
        return messageHandler;
    }
}
