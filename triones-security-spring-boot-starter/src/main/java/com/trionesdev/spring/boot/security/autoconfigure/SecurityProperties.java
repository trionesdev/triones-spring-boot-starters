package com.trionesdev.spring.boot.security.autoconfigure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("triones.security")
public class SecurityProperties {
    private String[] excludeMatchers = {};
    private String[] excludeGetMatchers = {};
    private String[] excludePostMatchers = {};
    private String[] excludePutMatchers = {};
    private String[] excludeDeleteMatchers = {};
    private String[] ignoreMatchers = {};

}
