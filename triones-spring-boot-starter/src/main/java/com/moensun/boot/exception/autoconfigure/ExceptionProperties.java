package com.moensun.boot.exception.autoconfigure;

import com.moensun.commons.exception.ExceptionResourceProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "triones.exception")
public class ExceptionProperties {

    private String[] resourcePaths;

    public void setResourcePaths(String[] resourcePaths) {
        this.resourcePaths = resourcePaths;
        ExceptionResourceProperties.setResourcePaths(resourcePaths);
    }
}
