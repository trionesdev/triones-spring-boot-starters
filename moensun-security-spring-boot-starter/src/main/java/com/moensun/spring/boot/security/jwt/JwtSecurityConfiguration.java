package com.moensun.spring.boot.security.jwt;

import com.google.common.collect.Lists;
import com.moensun.commons.context.actor.ActorContext;
import com.moensun.commons.core.jwt.JwtConfig;
import com.moensun.commons.core.jwt.JwtFacade;
import com.moensun.commons.security.spring.jwt.JwtAuthenticationEntryPoint;
import com.moensun.commons.security.spring.jwt.JwtAuthenticationFilter;
import com.moensun.commons.security.spring.jwt.JwtServerConfigurer;
import com.moensun.commons.security.spring.jwt.JwtTokenConfig;
import com.moensun.spring.boot.security.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@RequiredArgsConstructor
@Configuration(value = "com.moensun.spring.boot.security.jwt.JwtSecurityConfiguration")
@EnableConfigurationProperties(value = {
        JwtSecurityProperties.class
})
public class JwtSecurityConfiguration {
    private final ApplicationContext applicationContext;
    private final ActorContext actorContext;
    private final SecurityProperties securityProperties;
    private final JwtSecurityProperties jwtSecurityProperties;

    private final String[] ignoreMatchers = {"/favicon.ico", "/v3/api-docs/**", "/v2/api-docs", "/webjars/**", "/swagger-resources/**",
            "/swagger-ui/**", "/swagger-ui.html", "/actuator/**", "/websocket/**"};

    @Bean
    public JwtFacade jwtFacade() {
        return new JwtFacade(JwtConfig.builder().secret(jwtSecurityProperties.getSecret()).expiration(jwtSecurityProperties.getExpiration()).build());
    }

//    @Bean
//    WebSecurityCustomizer webSecurityCustomizer() {
//        String[] ignoreMatchers = {"/favicon.ico", "/v3/api-docs/**", "/v2/api-docs", "/webjars/**", "/swagger-resources/**",
//                "/swagger-ui/**", "/swagger-ui.html", "/actuator/**", "/websocket/**"};
//        List<String> ignoreMatcherList = Lists.newArrayList(ignoreMatchers);
//        ignoreMatcherList.addAll(Lists.newArrayList(securityProperties.getIgnoreMatchers()));
//        return (web) -> web.ignoring().antMatchers(ignoreMatcherList.toArray(new String[ignoreMatcherList.size()]));
//    }

    @Bean
    SecurityFilterChain securityWebFilterChain(HttpSecurity http, JwtFacade jwtFacade) throws Exception {
        JwtTokenConfig jwtTokenConfig = JwtTokenConfig.builder()
                .local(jwtSecurityProperties.getLocal())
                .endpoint(jwtSecurityProperties.getEndpoint())
                .secret(jwtSecurityProperties.getSecret())
                .expiration(jwtSecurityProperties.getExpiration())
                .build();

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenConfig, jwtFacade, actorContext);
        JwtServerConfigurer<HttpSecurity> jwtServerConfigurer = new JwtServerConfigurer<>(applicationContext, jwtAuthenticationFilter);

        List<String> ignoreMatcherList = Lists.newArrayList(ignoreMatchers);
        ignoreMatcherList.addAll(Lists.newArrayList(securityProperties.getIgnoreMatchers()));

        http.csrf().disable()
                .anonymous()
                .and().authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers(ignoreMatcherList.toArray(new String[ignoreMatcherList.size()])).permitAll()
                                .antMatchers(securityProperties.getExcludeMatchers()).permitAll()
                                .antMatchers(HttpMethod.GET, securityProperties.getExcludeGetMatchers()).permitAll()
                                .antMatchers(HttpMethod.POST, securityProperties.getExcludePostMatchers()).permitAll()
                                .antMatchers(HttpMethod.PUT, securityProperties.getExcludePutMatchers()).permitAll()
                                .antMatchers(HttpMethod.DELETE, securityProperties.getExcludeDeleteMatchers()).permitAll()
                                .anyRequest().authenticated()

                )
                .apply(jwtServerConfigurer)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint());
        return http.build();
    }

}
