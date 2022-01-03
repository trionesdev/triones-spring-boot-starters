package com.moensun.spring.boot.core;

import com.moensun.commons.core.spring.event.act.*;
import com.moensun.commons.core.spring.permission.act.ActPermissionAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration(value = "com.moensun.spring.boot.core.CoreAutoConfiguration")
public class CoreAutoConfiguration {

    @Bean
    public ActEventBeforeAspect actEventBeforeAspect(){
        return new ActEventBeforeAspect();
    }

    @Bean
    public ActEventAfterAspect actEventAfterAspect(){
        return new ActEventAfterAspect();
    }

    @Bean
    public ActEventAroundAspect actEventAroundAspect(){
        return new ActEventAroundAspect();
    }

    @Bean
    public ActEventAfterReturningAspect actEventAfterReturningAspect(){
        return new ActEventAfterReturningAspect();
    }

    @Bean
    public ActEventAfterThrowingAspect actEventAfterThrowingAspect(){
        return new ActEventAfterThrowingAspect();
    }

    @Bean
    public ActPermissionAspect actPermissionAspect(){
        return new ActPermissionAspect();
    }
}
