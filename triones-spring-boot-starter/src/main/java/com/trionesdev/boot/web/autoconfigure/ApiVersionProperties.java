package com.trionesdev.boot.web.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "triones.api-version")
public class ApiVersionProperties {
    private Mode mode;
    private String pathVersionName = "version";
    private String headerVersionName = "X-Version";

    public enum Mode {
        path,
        header
    }

}
