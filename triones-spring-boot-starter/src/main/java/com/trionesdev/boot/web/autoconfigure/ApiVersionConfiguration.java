package com.trionesdev.boot.web.autoconfigure;

import com.trionesdev.spring.web.apiversion.mvc.HeaderApiVersionRequestMappingHandlerMapping;
import com.trionesdev.spring.web.apiversion.mvc.PathApiVersionRequestMappingHandlerMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


@RequiredArgsConstructor
@EnableConfigurationProperties(ApiVersionProperties.class)
@Configuration
@ConditionalOnWebApplication
public class ApiVersionConfiguration {
    private final ApiVersionProperties apiVersionProperties;

    @Bean
    @ConditionalOnProperty(name = "triones.apiVersion.mode", havingValue = "path")
    public WebMvcRegistrations pathVersionWebMvcRegistrations() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new PathApiVersionRequestMappingHandlerMapping(apiVersionProperties.getPathVersionName());
            }
        };
    }

    @Bean
    @ConditionalOnProperty(name = "triones.apiVersion.mode", havingValue = "header")
    public WebMvcRegistrations headerVersionWebMvcRegistrations() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new HeaderApiVersionRequestMappingHandlerMapping(apiVersionProperties.getHeaderVersionName());
            }
        };
    }

}
