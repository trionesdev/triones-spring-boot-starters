package com.trionesdev.security.spring.web.autoconfigure;

import com.trionesdev.spring.security.AuthType;
import com.trionesdev.spring.security.TokenStyle;
import com.trionesdev.spring.security.TokenType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Data
@ConfigurationProperties("triones.security")
public class SecurityProperties {
    private String headerKey = AUTHORIZATION;
    private String queryParamKey = "token";
    private AuthType authType = AuthType.jwt;
    private TokenType tokenType = TokenType.jwt;
    private TokenStyle tokenStyle = TokenStyle.uuid;
    private Boolean enableRefresh = false;
    private String secret = "trionesdev_secret";
    private int expires = 86400;
    private int refreshExpires = 2592000;
    private String[] ignoreMatchers = {};
    private AuthorizeRequestProperties authorizeRequest = new AuthorizeRequestProperties();


}
