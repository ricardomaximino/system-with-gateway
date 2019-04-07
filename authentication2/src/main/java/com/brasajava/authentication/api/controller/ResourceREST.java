package com.brasajava.authentication.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brasajava.authentication.api.dto.MessageDTO;

import reactor.core.publisher.Mono;

@RestController
public class ResourceREST {

  @GetMapping("/resource/user")
  @PreAuthorize("hasRole('USER')")
  public Mono<ResponseEntity<MessageDTO>> user() {
    return Mono.just(ResponseEntity.ok(new MessageDTO("Content for user")));
  }

  @GetMapping("/resource/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public Mono<ResponseEntity<MessageDTO>> admin() {
    return Mono.just(ResponseEntity.ok(new MessageDTO("Content for admin")));
  }

  @GetMapping("/resource/user-or-admin")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public Mono<ResponseEntity<MessageDTO>> userOrAdmin() {
    return Mono.just(ResponseEntity.ok(new MessageDTO("Content for user or admin")));
  }
}
