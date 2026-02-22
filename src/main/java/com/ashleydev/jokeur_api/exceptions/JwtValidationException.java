package com.ashleydev.jokeur_api.exceptions;

public class JwtValidationException extends RuntimeException {

  public JwtValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
