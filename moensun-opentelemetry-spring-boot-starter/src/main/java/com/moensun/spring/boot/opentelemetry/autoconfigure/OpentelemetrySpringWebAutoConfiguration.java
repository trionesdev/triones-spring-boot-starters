package com.moensun.spring.boot.opentelemetry.autoconfigure;

import com.moensun.opentelemetry.tag.Tags;
import com.moensun.opentelemetry.spring.telemetry.configuration.TelemetryAutoConfiguration;
import com.moensun.opentelemetry.spring.web.SkipPatternAutoConfiguration;
import com.moensun.opentelemetry.spring.web.WebTelemetryProperties;
import com.moensun.opentelemetry.web.servlet.filter.ServletFilterSpanDecorator;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnBean(OpenTelemetry.class)
@AutoConfigureAfter({TelemetryAutoConfiguration.class, SkipPatternAutoConfiguration.class})
@EnableConfigurationProperties(WebTelemetryProperties.class)
@ConditionalOnClass(WebMvcConfigurer.class)
@ConditionalOnProperty(name = "opentelemetry.spring.web.enabled", havingValue = "true", matchIfMissing = true)
public class OpentelemetrySpringWebAutoConfiguration {

    @Bean
    public ServletFilterSpanDecorator servletFilterSpanDecorator(){
        return new ServletFilterSpanDecorator() {
            @Override
            public void onRequest(HttpServletRequest httpServletRequest, Span span) {
                span.setAttribute(Tags.COMPONENT, "java-web-servlet");
                span.setAttribute(Tags.HTTP_METHOD, httpServletRequest.getMethod());
                span.setAttribute(Tags.HTTP_URL, httpServletRequest.getRequestURL().toString());
            }

            @Override
            public void onResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Span span) {
                span.setAttribute(Tags.HTTP_STATUS, httpServletResponse.getStatus());
            }

            @Override
            public void onError(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Throwable exception, Span span) {
                span.setAttribute(Tags.ERROR, Boolean.TRUE);
                span.recordException(exception);
                if (httpServletResponse.getStatus() == HttpServletResponse.SC_OK) {
                    span.setAttribute(Tags.HTTP_STATUS, 500);
                }
            }

            @Override
            public void onTimeout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, long timeout, Span span) {
                Map<String, Object> timeoutLogs = new HashMap<>(2);
                timeoutLogs.put("event", "timeout");
                timeoutLogs.put("timeout", timeout);

            }
        };
    }

}
