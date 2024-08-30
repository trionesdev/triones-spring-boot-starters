package com.trionesdev.boot.lock.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "triones.lock")
public class LockProperties {
    private Mode mode = Mode.THREAD;

    public enum Mode {
        THREAD,
        REDIS
    }
}
