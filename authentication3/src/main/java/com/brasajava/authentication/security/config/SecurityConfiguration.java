package com.brasajava.authentication.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfiguration {
  private static final String[] AUTH_WHITELIST = {"/login"};

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

    return http.authorizeExchange()
        .pathMatchers(AUTH_WHITELIST)
        .permitAll()
        .anyExchange()
        .authenticated()
        .and()
        .csrf()
        .disable()
        .logout()
        .disable()
        .httpBasic()
        .and()
        .build();
  }
}
