package com.moensun.spring.boot.core;

import com.moensun.commons.core.spring.event.act.ActEventAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration(value = "com.moensun.spring.boot.core.CoreAutoConfiguration")
public class CoreAutoConfiguration {

    @Bean
    public ActEventAspect actEventAspect(){
        return new ActEventAspect();
    }

}
