package com.moensun.spring.boot.propagate;

import io.jaegertracing.internal.reporters.CompositeReporter;
import io.jaegertracing.spi.Reporter;
import io.opentracing.contrib.java.spring.jaeger.starter.JaegerAutoConfiguration;
import io.opentracing.contrib.java.spring.jaeger.starter.ReporterAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

@Configuration(value = "com.moensun.spring.boot.propagate.propagateJaegerConfiguration")
@ConditionalOnClass(io.jaegertracing.internal.JaegerTracer.class)
@ConditionalOnMissingBean(io.opentracing.Tracer.class)
@ConditionalOnProperty(value = "opentracing.jaeger.reporter.enabled", havingValue = "false", matchIfMissing = true)
@AutoConfigureBefore(JaegerAutoConfiguration.class)
@EnableConfigurationProperties(PropagateJaegerConfProperties.class)
public class PropagateJaegerConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public Reporter reporter(@Autowired(required = false) ReporterAppender reporterAppender) {

        List<Reporter> reporters = new LinkedList<>();
        if (reporterAppender != null) {
            reporterAppender.append(reporters);
        }

        return new CompositeReporter(reporters.toArray(new Reporter[reporters.size()]));
    }

}
