package com.moensun.boot.autoconfigure.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("ms.security")
public class SecurityProperties {
    private String[] excludeMatchers = {};
    private String[] excludeGetMatchers = {};
    private String[] excludePostMatchers = {};
    private String[] excludePutMatchers = {};
    private String[] excludeDeleteMatchers = {};
    private String[] ignoreMatchers = {};

}
