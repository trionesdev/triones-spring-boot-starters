package com.trionesdev.boot.autoconfigure.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("triones.security.jwt")
public class JwtSecurityProperties {
    private Boolean enabled;
    private Boolean local = true;
    private String endpoint;
    private String secret = "secret1234567890qwertyuiopasdfghg";
    private int expiration;
}
