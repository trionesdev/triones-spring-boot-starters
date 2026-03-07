package com.trionesdev.spring.boot.security.autoconfigure;

import lombok.Data;

@Data
public class AuthorizeRequestProperties {
    private AuthorizeType authorizeType = AuthorizeType.authenticated;
    private RequestMatcherVariable variable;
    private String[] role = {};
    private String[] authority = {};
    private RequestMatcherProperties[] requestMatchers = {};
}
