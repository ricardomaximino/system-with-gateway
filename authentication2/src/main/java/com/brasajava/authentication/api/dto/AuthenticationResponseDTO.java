package com.brasajava.authentication.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponseDTO implements Serializable {

  private static final long serialVersionUID = 1036725829672077285L;
  private final String token;
}
