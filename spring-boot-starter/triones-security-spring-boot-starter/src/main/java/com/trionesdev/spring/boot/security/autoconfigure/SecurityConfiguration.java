package com.trionesdev.spring.boot.security.autoconfigure;

import com.trionesdev.spring.security.*;
import com.trionesdev.spring.security.jwt.JwtAuthenticationFilter;
import com.trionesdev.spring.security.jwt.JwtTokenManager;
import com.trionesdev.spring.security.token.TokenManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(value = {
        SecurityProperties.class
})
public class SecurityConfiguration {

    private final SecurityProperties properties;

    private final String[] ignoreMatchers = {"/favicon.ico", "/scalar", "/v3/api-docs/**", "/v2/api-docs", "/webjars/**", "/swagger-resources/**",
            "/swagger-ui/**", "/swagger-ui.html", "/actuator/**"};

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        String[] ignoreMatchers = ArrayUtils.addAll(this.ignoreMatchers, properties.getIgnoreMatchers());
        return (web) -> web.ignoring().requestMatchers(ignoreMatchers);
    }

    @Bean
    public SecurityTokenConfig securityTokenConfig() {
        return SecurityTokenConfig.builder()
                .tokenKey(properties.getTokenKey())
                .secret(properties.getSecret())
                .expires(properties.getExpires())
                .refreshExpires(properties.getRefreshExpires())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(TokenManager.class)
    public TokenManager tokenManager(SecurityTokenConfig config) {
        return new JwtTokenManager(config);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityTokenConfig config, AuthProcessor processor, AuthorityManager authorityManager) throws Exception {
        var authFilter = new JwtAuthenticationFilter(config);
        http.authorizeHttpRequests(authorizeHttpRequests -> {
                            Arrays.stream(properties.getAuthorizeRequest().getRequestMatchers()).forEach(requestMatcher -> {
                                switch (requestMatcher.getAuthorizeType()) {
                                    case permitAll ->
                                            authorizeHttpRequests.requestMatchers(requestMatcher.getMethod(), requestMatcher.getPatterns()).permitAll();
                                    case denyAll ->
                                            authorizeHttpRequests.requestMatchers(requestMatcher.getMethod(), requestMatcher.getPatterns()).denyAll();
                                    case authenticated ->
                                            authorizeHttpRequests.requestMatchers(requestMatcher.getMethod(), requestMatcher.getPatterns()).authenticated();
                                    case anonymous ->
                                            authorizeHttpRequests.requestMatchers(requestMatcher.getMethod(), requestMatcher.getPatterns()).anonymous();
                                    case hasVariable ->
                                            authorizeHttpRequests.requestMatchers(requestMatcher.getMethod(), requestMatcher.getPatterns()).hasVariable(requestMatcher.getVariable().getName()).equalTo((authentication -> requestMatcher.getVariable().getValue()));
                                    case hasRole ->
                                            authorizeHttpRequests.requestMatchers(requestMatcher.getMethod(), requestMatcher.getPatterns()).hasRole(Arrays.toString(requestMatcher.getRole()));
                                    case hasAnyRole ->
                                            authorizeHttpRequests.requestMatchers(requestMatcher.getMethod(), requestMatcher.getPatterns()).hasAnyRole(requestMatcher.getRole());
                                }
                            });
                            switch (properties.getAuthorizeRequest().getAuthorizeType()) {
                                case denyAll -> authorizeHttpRequests.anyRequest().denyAll();
                                case authenticated -> authorizeHttpRequests.anyRequest().authenticated();
                                case anonymous -> authorizeHttpRequests.anyRequest().anonymous();
                                case hasVariable ->
                                        authorizeHttpRequests.anyRequest().hasVariable(properties.getAuthorizeRequest().getVariable().getName()).equalTo((authentication -> properties.getAuthorizeRequest().getVariable().getValue()));
                                case hasRole ->
                                        authorizeHttpRequests.anyRequest().hasRole(Arrays.toString(properties.getAuthorizeRequest().getRole()));
                                case hasAnyRole ->
                                        authorizeHttpRequests.anyRequest().hasAnyRole(properties.getAuthorizeRequest().getRole());
                                case hasAuthority ->
                                        authorizeHttpRequests.anyRequest().hasAuthority(Arrays.toString(properties.getAuthorizeRequest().getAuthority()));
                                case hasAnyAuthority ->
                                        authorizeHttpRequests.anyRequest().hasAnyAuthority(properties.getAuthorizeRequest().getAuthority());
                                default -> authorizeHttpRequests.anyRequest().permitAll();
                            }
                        }
                )
                .with(new GeneralAuthenticationConfigurer<>(authFilter, processor), Customizer.withDefaults())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(new JsonAuthenticationEntryPoint())
                                .accessDeniedHandler(new JsonAccessDeniedHandler()))
        ;
        return http.build();
    }

}
