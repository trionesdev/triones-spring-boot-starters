package com.trionesdev.boot.lock.autoconfigure;

import com.trionesdev.commons.lock.TrionesLockTemplate;
import com.trionesdev.commons.lock.redis.RedisLockClient;
import com.trionesdev.commons.lock.thread.ThreadLockClient;
import com.trionesdev.spring.lock.LockAspect;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration(value = "com.trionesdev.boot.lock.autoconfigure.LockAutoConfiguration")
@EnableConfigurationProperties(value = {
        LockProperties.class
})
public class LockAutoConfiguration {
    private final LockProperties lockProperties;
    private final RedissonClient redissonClient;

    @Autowired
    public LockAutoConfiguration(LockProperties lockProperties, @Autowired(required = false) RedissonClient redissonClient) {
        this.lockProperties = lockProperties;
        this.redissonClient = redissonClient;
    }


    @Bean
    public TrionesLockTemplate trionesLockTemplate() {
        if (lockProperties.getMode() == LockProperties.Mode.REDIS) {
            return new RedisLockClient(redissonClient);
        } else {
            return new ThreadLockClient();
        }
    }

    @Bean
    public LockAspect lockAspect(TrionesLockTemplate lockTemplate) {
        return new LockAspect(lockTemplate);
    }


}
