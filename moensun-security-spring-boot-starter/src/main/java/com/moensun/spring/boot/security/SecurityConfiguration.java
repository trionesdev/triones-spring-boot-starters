package com.moensun.spring.boot.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration(value = "com.moensun.spring.boot.security.SecurityConfiguration")
@EnableWebSecurity(debug = true)
@EnableConfigurationProperties(value = {
        SecurityProperties.class
})
public class SecurityConfiguration {
}
