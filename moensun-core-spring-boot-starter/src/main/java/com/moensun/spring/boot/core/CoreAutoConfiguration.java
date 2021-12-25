package com.moensun.spring.boot.core;

import com.moensun.commons.core.spring.event.act.AbsActEventHandler;
import com.moensun.commons.core.spring.event.act.ActEventAspect;
import com.moensun.commons.core.spring.event.act.ActEventHandlerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration(value = "com.moensun.spring.boot.core.CoreAutoConfiguration")
public class CoreAutoConfiguration {

    @Bean
    public ActEventHandlerFactory actEventHandlerFactory(List<AbsActEventHandler> actEventHandlers){
        return new ActEventHandlerFactory(actEventHandlers);
    }

    @Bean
    public ActEventAspect actEventAspect(){
        return new ActEventAspect();
    }

}
