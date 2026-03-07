package com.trionesdev.spring.boot.security.autoconfigure;

import lombok.Data;
import org.springframework.http.HttpMethod;

@Data
public class RequestMatcherProperties {
    private HttpMethod method;
    private String[] patterns = {};
    private AuthorizeType authorizeType;
    private RequestMatcherVariable variable;
    private String[] role = {};
    private String[] authority = {};
}
