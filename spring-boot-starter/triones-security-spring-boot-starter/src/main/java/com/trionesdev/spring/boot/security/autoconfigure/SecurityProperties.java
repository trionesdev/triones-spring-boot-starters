package com.trionesdev.spring.boot.security.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("triones.security")
public class SecurityProperties {
    private String tokenKey = "token";
    private String secret = "trionesdev_secret";
    private int expires = 86400;
    private int refreshExpires = 2592000;
    private String[] ignoreMatchers = {};
    private AuthorizeRequestProperties authorizeRequest = new AuthorizeRequestProperties();
}
