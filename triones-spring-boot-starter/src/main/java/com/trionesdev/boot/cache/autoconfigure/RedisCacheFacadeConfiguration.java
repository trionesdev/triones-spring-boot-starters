package com.trionesdev.boot.cache.autoconfigure;

import com.trionesdev.spring.cache.RedisCacheFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
@Configuration
@ConditionalOnClass({RedisTemplate.class})
@ConditionalOnMissingBean({RedisCacheFacade.class})
@Conditional({CacheFacadeCondition.class})
public class RedisCacheFacadeConfiguration {

    @Bean
    public <K, V> RedisCacheFacade<K, V> cacheFacade(RedisTemplate<K, V> redisTemplate) {
        return new RedisCacheFacade<>(redisTemplate);
    }

}
