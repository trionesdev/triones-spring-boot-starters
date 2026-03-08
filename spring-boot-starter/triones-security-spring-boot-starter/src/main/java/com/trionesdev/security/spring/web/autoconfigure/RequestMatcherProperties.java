package com.trionesdev.security.spring.web.autoconfigure;

import lombok.Data;
import org.springframework.http.HttpMethod;

@Data
public class RequestMatcherProperties {
    private HttpMethod method;
    private String[] patterns = {};
    private AuthorizeType authorizeType = AuthorizeType.permitAll;
    private RequestMatcherVariable variable;
    private String[] role = {};
    private String[] authority = {};
}
