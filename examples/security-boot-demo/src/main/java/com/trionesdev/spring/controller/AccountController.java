package com.trionesdev.spring.controller;

import com.trionesdev.spring.security.token.SecurityToken;
import com.trionesdev.spring.security.token.TokenDefinition;
import com.trionesdev.spring.security.token.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping
@RestController
public class AccountController {
    private final TokenManager tokenManager;
    @PostMapping("/login")
    public SecurityToken login() {
        return tokenManager.createToken(TokenDefinition.builder().subject("123").build());
    }

    @GetMapping("/profile")
    public String logout( ) {
        return "profile";
    }
}
