package com.moensun.spring.boot.propagate;

import com.moensun.spring.boot.propagate.scope.MSMDCScopeManager;
import io.opentracing.contrib.java.spring.jaeger.starter.TracerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "com.moensun.spring.boot.propagate.propagateAutoConfiguration")
public class PropagateAutoConfiguration {
    @Bean
    public TracerBuilderCustomizer tracerBuilderCustomizer() {
        return builder -> builder.withScopeManager(new MSMDCScopeManager.Builder().build());
    }
}
