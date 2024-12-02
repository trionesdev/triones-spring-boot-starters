package com.trionesdev.boot.cache.autoconfigure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@Conditional({CacheFacadeCondition.class})
public class CouchbaseCacheFacadeConfiguration {
}
