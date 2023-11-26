package com.moensun.boot.autoconfigure.exception;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "triones.spring.exception")
public class ExceptionProperties {

    private String[] resourcePaths;
}
