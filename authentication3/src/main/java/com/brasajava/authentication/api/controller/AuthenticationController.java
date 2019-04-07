package com.brasajava.authentication.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.brasajava.authentication.api.dto.AuthenticationRequestDTO;
import com.brasajava.authentication.api.dto.AuthenticationResponseDTO;
import com.brasajava.authentication.utils.JwtTokenUtil;

import reactor.core.publisher.Mono;

@RestController
public class AuthenticationController {

  private JwtTokenUtil jwtUtil;

  private PasswordEncoder passwordEncoder;

  private ReactiveUserDetailsService userService;

  public AuthenticationController(JwtTokenUtil jwtUtil, ReactiveUserDetailsService userRepository) {
    this.jwtUtil = jwtUtil;
    this.userService = userRepository;
    this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @PostMapping("/login")
  public Mono<ResponseEntity<?>> login(@RequestBody AuthenticationRequestDTO request) {
    return userService
        .findByUsername(request.getUsername())
        .map(
            userDetails -> {
              if (passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
                return ResponseEntity.ok(
                    new AuthenticationResponseDTO(jwtUtil.generateToken(userDetails)));
              } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
              }
            })
        .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
  }
}
