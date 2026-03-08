package com.trionesdev.security.spring.web.autoconfigure;

import com.trionesdev.spring.security.*;
import com.trionesdev.spring.security.token.DefaultTokenStorage;
import com.trionesdev.spring.security.token.TokenStorage;
import com.trionesdev.spring.security.web.*;
import com.trionesdev.spring.security.token.TokenManager;
import com.trionesdev.spring.security.web.jwt.JwtAuthenticationProvider;
import com.trionesdev.spring.security.web.jwt.JwtTokenManager;
import com.trionesdev.spring.security.web.token.DefaultTokenManager;
import com.trionesdev.spring.security.web.token.TokenAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.Objects;
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
                .tokenStyle(properties.getTokenStyle())
                .authType(properties.getAuthType())
                .tokenType(properties.getTokenType())
                .enableRefresh(properties.getEnableRefresh())
                .secret(properties.getSecret())
                .build();
    }

    /**
     * 默认的Token存储
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(TokenStorage.class)
    public TokenStorage tokenStorage() {
        return new DefaultTokenStorage();
    }

    /**
     * 默认的Token管理器
     *
     * @param config
     * @param tokenStorage
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(TokenManager.class)
    public TokenManager tokenManager(SecurityTokenConfig config, TokenStorage tokenStorage) {
        if (Objects.equals(config.getAuthType(), AuthType.jwt)) {
            return new JwtTokenManager(config, tokenStorage);
        } else if (Objects.equals(config.getAuthType(), AuthType.apiKey) || Objects.equals(config.getAuthType(), AuthType.bearerToken)) {
            if (Objects.equals(config.getTokenType(), TokenType.jwt)) {
                return new JwtTokenManager(config, tokenStorage);
            } else {
                return new DefaultTokenManager(config, tokenStorage);
            }
        }
        return new DefaultTokenManager(config, tokenStorage);
    }

    /**
     * 默认的认证执行器
     *
     * @param config
     * @param authorityManager
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(AuthenticationExecutor.class)
    public AuthenticationExecutor authenticationExecutor(
            ObjectProvider<SecurityTokenConfig> config,
            ObjectProvider<AuthorityManager> authorityManager
    ) {
        AbstractAuthenticationExecutor executor = new TokenAuthenticationExecutor(config.getIfAvailable());
        executor.setAuthorityManager(authorityManager.getIfAvailable());
        return executor;
    }

    /**
     * 默认的认证提供者
     *
     * @param config
     * @param authorityManager
     * @param tokenStorage
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(AuthenticationProvider.class)
    public AuthenticationProvider authenticationProvider(
            SecurityTokenConfig config,
            ObjectProvider<AuthorityManager> authorityManager,
            ObjectProvider<TokenStorage> tokenStorage
    ) {
        if (Objects.equals(config.getAuthType(), AuthType.jwt)) {
            return new JwtAuthenticationProvider(config, authorityManager.getIfAvailable(), tokenStorage.getIfAvailable());
        }
        return new TokenAuthenticationProvider(
                config,
                authorityManager.getIfAvailable(),
                tokenStorage.getIfAvailable()
        );
    }

    /**
     * 安全过滤器链
     *
     * @param http
     * @param authenticationExecutor
     * @param authenticationProvider
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            ObjectProvider<AuthenticationExecutor> authenticationExecutor,
            ObjectProvider<AuthenticationProvider> authenticationProvider
    ) throws Exception {

        GeneralAuthenticationConfigurer<HttpSecurity> authenticationConfigurer = new GeneralAuthenticationConfigurer<>(authenticationExecutor.getIfAvailable());
        authenticationInterceptor.ifAvailable(authenticationConfigurer::setAuthenticationInterceptor);
        // 认证提供者
        authenticationProvider.ifAvailable(http::authenticationProvider);
        // 禁用csrf
        http.csrf(CsrfConfigurer::disable);
        // 认证配置
        http.authorizeHttpRequests(authorizeHttpRequests -> {
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
        );
        http.with(authenticationConfigurer, Customizer.withDefaults());
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(new TokenAuthenticationEntryPoint())
                                .accessDeniedHandler(new TokenAccessDeniedHandler())
                )
        ;
        return http.build();
    }

}
