package com.trionesdev.boot.core.autoconfigure;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.trionesdev.commons.core.util.JsonUtils;
import com.trionesdev.spring.core.event.act.*;
import com.trionesdev.spring.core.permission.act.ActPermissionAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration(value = "com.trionesdev.autoconfigure.core.CoreAutoConfiguration")
@EnableConfigurationProperties(value = {AppProperties.class})
public class CoreAutoConfiguration implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ObjectMapper) {
            JsonUtils.setObjectMapper((ObjectMapper) bean);
        }
        return bean;
    }

    @Bean
    public ActEventBeforeAspect actEventBeforeAspect() {
        return new ActEventBeforeAspect();
    }

    @Bean
    public ActEventAfterAspect actEventAfterAspect() {
        return new ActEventAfterAspect();
    }

    @Bean
    public ActEventAroundAspect actEventAroundAspect() {
        return new ActEventAroundAspect();
    }

    @Bean
    public ActEventAfterReturningAspect actEventAfterReturningAspect() {
        return new ActEventAfterReturningAspect();
    }

    @Bean
    public ActEventAfterThrowingAspect actEventAfterThrowingAspect() {
        return new ActEventAfterThrowingAspect();
    }

    @Bean
    public ActPermissionAspect actPermissionAspect() {
        return new ActPermissionAspect();
    }
}
