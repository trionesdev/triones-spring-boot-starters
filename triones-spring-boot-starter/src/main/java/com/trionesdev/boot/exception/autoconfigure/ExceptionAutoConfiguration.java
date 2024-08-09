package com.trionesdev.boot.exception.autoconfigure;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration(value = "com.trionesdev.autoconfigure.exception.exceptionAutoConfiguration")
@EnableConfigurationProperties(
        value = {
                ExceptionProperties.class
        }
)
public class ExceptionAutoConfiguration {

}
