package com.brasajava.authentication.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.brasajava.authentication.security.config.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;

@Component
public class JwtTokenUtil implements Serializable {

  private static final long serialVersionUID = 3282402734200213844L;
  private Clock clock = DefaultClock.INSTANCE;
  private String secret;
  private Long expiration;

  public JwtTokenUtil(JwtConfig jwtConfig) {
    this.secret = jwtConfig.getSecret();
    this.expiration = jwtConfig.getExpiration();
  }

  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public Date getIssuedAtDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getIssuedAt);
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  public Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(clock.now());
  }

  private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
    return (lastPasswordReset != null && created.before(lastPasswordReset));
  }

  private Boolean ignoreTokenExpiration(String token) {
    // here you specify tokens, for that the expiration is ignored
    return false;
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("company", "brasajava");
    claims.put("year", "2014");
    List<String> roles = new ArrayList<>();
    userDetails.getAuthorities().stream().forEach(a -> roles.add(a.getAuthority()));
    claims.put("roles", roles);
    return doGenerateToken(claims, userDetails.getUsername());
  }

  private String doGenerateToken(Map<String, Object> claims, String subject) {
    final Date createdDate = clock.now();
    final Date expirationDate = calculateExpirationDate(createdDate);

    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(createdDate)
        .setExpiration(expirationDate)
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
    final Date created = getIssuedAtDateFromToken(token);
    return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
        && (!isTokenExpired(token) || ignoreTokenExpiration(token));
  }

  public String refreshToken(String token) {
    final Date createdDate = clock.now();
    final Date expirationDate = calculateExpirationDate(createdDate);

    final Claims claims = getAllClaimsFromToken(token);
    claims.setIssuedAt(createdDate);
    claims.setExpiration(expirationDate);

    return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  public Boolean validateToken(String token, String username) {
    return (username.equals(getUsernameFromToken(token)) && !isTokenExpired(token));
  }

  private Date calculateExpirationDate(Date createdDate) {
    return new Date(createdDate.getTime() + expiration * 1000);
  }
}
