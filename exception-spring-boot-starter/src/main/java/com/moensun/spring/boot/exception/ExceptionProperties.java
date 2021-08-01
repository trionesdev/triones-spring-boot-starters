package com.moensun.spring.boot.exception;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ms.spring.exception")
public class ExceptionProperties {

    private String[] resourcePaths;

    public String[] getResourcePaths() {
        return resourcePaths;
    }

    public void setResourcePaths(String[] resourcePaths) {
        this.resourcePaths = resourcePaths;
    }
}
