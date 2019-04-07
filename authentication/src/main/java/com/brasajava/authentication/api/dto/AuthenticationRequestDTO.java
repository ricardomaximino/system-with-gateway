package com.brasajava.authentication.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequestDTO implements Serializable {

  private static final long serialVersionUID = 1660667498822740676L;
  private String username;
  private String password;
}
