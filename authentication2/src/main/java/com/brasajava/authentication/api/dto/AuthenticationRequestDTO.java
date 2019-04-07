package com.brasajava.authentication.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequestDTO implements Serializable {

  private static final long serialVersionUID = -8760296423815171181L;
  private String username;
  private String password;
}
