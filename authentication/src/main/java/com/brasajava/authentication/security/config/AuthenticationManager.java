package com.brasajava.authentication.security.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.brasajava.authentication.enums.Role;
import com.brasajava.authentication.utils.JwtTokenUtil;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

  private JwtTokenUtil jwtUtil;

  public AuthenticationManager(JwtTokenUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    String authToken = authentication.getCredentials().toString();

    String username;
    try {
      username = jwtUtil.getUsernameFromToken(authToken);
    } catch (Exception e) {
      username = null;
    }
    if (jwtUtil.validateToken(authToken, username)) {
      Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
      List<String> rolesList = claims.get("roles", List.class);
      List<Role> roles = new ArrayList<>();
      for (String rolemap : rolesList) {
        roles.add(Role.valueOf(rolemap));
      }
      UsernamePasswordAuthenticationToken auth =
          new UsernamePasswordAuthenticationToken(
              username,
              null,
              roles
                  .stream()
                  .map(authority -> new SimpleGrantedAuthority(authority.name()))
                  .collect(Collectors.toList()));
      return Mono.just(auth);
    } else {
      return Mono.empty();
    }
  }
}
