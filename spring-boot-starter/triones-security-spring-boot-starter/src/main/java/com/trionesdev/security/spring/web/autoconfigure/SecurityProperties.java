package com.trionesdev.security.spring.web.autoconfigure;

import com.trionesdev.spring.security.SecurityTokenConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Data
@ConfigurationProperties("triones.security")
public class SecurityProperties {
    private String headerKey = AUTHORIZATION;
    private String queryParamKey = "token";
    private SecurityTokenConfig.Jwt jwt;
    private int expires = 86400;
    private int refreshExpires = 2592000;
    private String[] ignoreMatchers = {};
    private AuthorizeRequestProperties authorizeRequest = new AuthorizeRequestProperties();


}
