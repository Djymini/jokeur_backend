package com.ashleydev.jokeur_api.security;

import com.ashleydev.jokeur_api.exceptions.JwtValidationException;
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private int jwtExpirationMs;

  private SecretKey key;

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(UserEntity owner) {
    return Jwts.builder()
      .setSubject(owner.getEmail())
      .claim("role", owner.getRole().name())
      .setIssuedAt(new Date())
      .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }

  public String getEmailFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      throw new JwtValidationException("Token expiré", e);
    } catch (MalformedJwtException e) {
      throw new JwtValidationException("Token mal formé", e);
    } catch (SecurityException e) {
      throw new JwtValidationException("Signature invalide", e);
    } catch (UnsupportedJwtException e) {
      throw new JwtValidationException("Format JWT non supporté", e);
    } catch (IllegalArgumentException e) {
      throw new JwtValidationException("Token vide ou invalide", e);
    }
  }
}
