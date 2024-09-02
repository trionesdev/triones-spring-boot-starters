package com.trionesdev.boot.lock.autoconfigure;

import com.trionesdev.commons.lock.TrionesLockTemplate;
import com.trionesdev.commons.lock.redis.RedisLockTemplate;
import com.trionesdev.commons.lock.thread.ThreadLockTemplate;
import com.trionesdev.spring.lock.LockAspect;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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


    @ConditionalOnProperty(prefix = "triones.lock", value = "mode", havingValue = "THREAD")
    @Bean
    public ThreadLockTemplate threadLockTemplate() {
        return new ThreadLockTemplate();
    }

    @ConditionalOnMissingBean(TrionesLockTemplate.class)
    @ConditionalOnProperty(prefix = "triones.lock", value = "mode", havingValue = "REDIS")
    @Bean
    public RedisLockTemplate redisLockTemplate() {
        return new RedisLockTemplate(redissonClient);
    }


    @Bean
    public LockAspect lockAspect(TrionesLockTemplate lockTemplate) {
        return new LockAspect(lockTemplate);
    }


}
