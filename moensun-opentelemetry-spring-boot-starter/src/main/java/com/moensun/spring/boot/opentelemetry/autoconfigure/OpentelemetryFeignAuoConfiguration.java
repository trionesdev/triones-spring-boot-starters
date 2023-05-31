package com.moensun.spring.boot.opentelemetry.autoconfigure;

import com.moensun.commons.context.actor.ActorContext;
import com.moensun.opentelemetry.feign.FeignSpanDecorator;
import com.moensun.opentelemetry.feign.FeignTags;
import com.moensun.opentelemetry.spring.telemetry.configuration.TelemetryAutoConfiguration;
import feign.Request;
import feign.Response;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@ConditionalOnBean(OpenTelemetry.class)
@AutoConfigureAfter({TelemetryAutoConfiguration.class})
@ConditionalOnProperty(name = "opentelemetry.spring.cloud.feign.enabled", havingValue = "true", matchIfMissing = true)
public class OpentelemetryFeignAuoConfiguration {

    private final ActorContext actorContext;

    @Bean
    public FeignSpanDecorator feignSpanDecorator(){
        return new FeignSpanDecorator() {
            @Override
            public void onRequest(Request request, Request.Options options, Span span) {

                span.setAttribute(FeignTags.COMPONENT, "feign");
                span.setAttribute(FeignTags.HTTP_URL, request.url());
                span.setAttribute(FeignTags.HTTP_METHOD, request.httpMethod().name());
            }

            @Override
            public void onResponse(Response response, Request.Options options, Span span) {
                span.setAttribute(FeignTags.HTTP_STATUS, response.status());
                span.setStatus(StatusCode.OK);
            }

            @Override
            public void onError(Exception exception, Request request, Span span) {
                span.setAttribute(FeignTags.ERROR, Boolean.TRUE);
                span.setStatus(StatusCode.ERROR);
                span.recordException(exception);
            }
        };
    }

}
