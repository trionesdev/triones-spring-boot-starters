package com.moensun.spring.boot.feign;

import com.moensun.commons.feign.codec.DefaultErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MsFeignConfiguration {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new DefaultErrorDecoder();
    }

}
