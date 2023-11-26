package com.moensun.boot.exception.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "triones.exception")
public class ExceptionProperties {

    private String[] resourcePaths;
}
