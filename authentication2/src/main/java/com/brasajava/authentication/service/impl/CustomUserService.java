package com.brasajava.authentication.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class CustomUserService implements ReactiveUserDetailsService {

  private PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private UserDetails user(String u, String... roles) {
    return User.builder()
        .username(u)
        .password(encoder.encode("password"))
        .authorities(roles)
        .build();
  }

  private final Collection<UserDetails> users =
      new ArrayList<>(
          Arrays.asList(
              user("thor", "ROLE_ADMIN"),
              user("loki", "ROLE_USER"),
              user("zeus", "ROLE_ADMIN", "ROLE_USER")));

  @Override
  public Mono<UserDetails> findByUsername(String username) {
    Optional<UserDetails> maybeUser =
        users.stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();
    return maybeUser.map(Mono::just).orElse(Mono.empty());
  }
}
