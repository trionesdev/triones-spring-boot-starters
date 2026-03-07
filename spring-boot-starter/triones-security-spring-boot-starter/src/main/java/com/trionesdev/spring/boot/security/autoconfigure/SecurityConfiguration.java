package com.trionesdev.spring.boot.security.autoconfigure;

import com.trionesdev.spring.security.*;
import com.trionesdev.spring.security.jwt.JwtAuthenticationExecutor;
import com.trionesdev.spring.security.jwt.JwtTokenManager;
import com.trionesdev.spring.security.token.TokenManager;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(value = {
        SecurityProperties.class
})
public class SecurityConfiguration {

    private final SecurityProperties properties;
    private final ObjectProvider<AuthenticationInterceptor> authenticationInterceptor;
    private final ObjectProvider<AuthorityManager> authorityManager;

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
                .headerKey(properties.getHeaderKey())
                .queryParamKey(properties.getQueryParamKey())
                .expires(properties.getExpires())
                .refreshExpires(properties.getRefreshExpires())
                .jwt(properties.getJwt())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(TokenManager.class)
    public TokenManager tokenManager(SecurityTokenConfig config) {
        return new JwtTokenManager(config);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityTokenConfig config) throws Exception {
        var authExecutor = new JwtAuthenticationExecutor(config);
        authExecutor.setAuthorityManager(authorityManager.getIfAvailable());

        GeneralAuthenticationConfigurer<HttpSecurity> authenticationConfigurer = new GeneralAuthenticationConfigurer<>(authExecutor);
        authenticationConfigurer.setAuthenticationInterceptor(authenticationInterceptor.getIfAvailable());

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests -> {
                            Optional.ofNullable(properties.getAuthorizeRequest()).map(AuthorizeRequestProperties::getRequestMatchers).ifPresent(requestMatchers -> {
                                if (ArrayUtils.isNotEmpty(requestMatchers)) {
                                    Arrays.stream(requestMatchers).forEach(requestMatcher -> {
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
                                }
                            });
                            Optional.ofNullable(properties.getAuthorizeRequest()).ifPresent(authorizeRequest -> {
                                switch (authorizeRequest.getAuthorizeType()) {
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
                            });
                        }
                )
                .with(authenticationConfigurer, Customizer.withDefaults())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(new JsonAuthenticationEntryPoint())
                                .accessDeniedHandler(new JsonAccessDeniedHandler())
                )
        ;
        return http.build();
    }

}
