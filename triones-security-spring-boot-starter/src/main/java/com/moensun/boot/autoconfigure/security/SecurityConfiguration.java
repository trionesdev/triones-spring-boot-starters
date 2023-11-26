package com.moensun.boot.autoconfigure.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration(value = "com.moensun.boot.autoconfigure.security.SecurityConfiguration")
@EnableWebSecurity
@EnableConfigurationProperties(value = {
        SecurityProperties.class
})
public class SecurityConfiguration {
}
