package com.trionesdev.boot.lock.autoconfigure;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class LockConfigurations {
    private static final Map<LockType, String> MAPPINGS;

    static {
        Map<LockType, String> mappings = new EnumMap<>(LockType.class);
        mappings.put(LockType.THREAD, ThreadLockConfiguration.class.getName());
        mappings.put(LockType.REDIS, RedisLockConfiguration.class.getName());
        MAPPINGS = Collections.unmodifiableMap(mappings);
    }

}
