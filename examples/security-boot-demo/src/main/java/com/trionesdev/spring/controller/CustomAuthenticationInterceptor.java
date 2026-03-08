package com.trionesdev.spring.controller;

import com.alibaba.fastjson2.JSON;
import com.trionesdev.spring.security.AuthenticationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationInterceptor implements AuthenticationInterceptor {
    @Override
    public void before(Authentication authentication) {
        log.info("before authentication: {}", JSON.toJSONString(authentication));
        System.out.println("before");
    }

    @Override
    public void after(Authentication authentication) {
        log.info("after authentication: {}", JSON.toJSONString(authentication));
        System.out.println("after");

    }
}
