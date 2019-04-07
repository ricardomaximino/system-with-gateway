package com.brasajava.authentication.security.config;

import java.util.HashMap;
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

  public JwtConfig() {
    this.header = "Authorization";
    this.secret = "mySecret";
    this.expiration = 604800L;
    this.route = new Route();
    Map<String, String> map = new HashMap<>();
    map.put("path", "/auth");
    map.put("refresh", "/refresh");
    this.route.setAuthentication(map);
  }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Route {
  private Map<String, String> authentication;
}
