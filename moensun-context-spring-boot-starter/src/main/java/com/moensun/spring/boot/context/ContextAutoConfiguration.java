package com.moensun.spring.boot.context;

import com.moensun.commons.context.operator.OperateContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration(value = "com.moensun.spring.boot.context.contextAutoConfiguration")
public class ContextAutoConfiguration {

    @Bean
    public OperateContext operateContext(){
        return new OperateContext();
    }
}
