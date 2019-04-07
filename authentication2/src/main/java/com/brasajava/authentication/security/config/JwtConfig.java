package com.brasajava.authentication.security.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
  private String header;
  private String secret;
  private Long expiration;
  private Route route;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Route {
  private Map<String, String> authentication;
}
