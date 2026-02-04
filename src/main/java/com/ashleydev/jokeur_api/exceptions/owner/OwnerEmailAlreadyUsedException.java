package com.ashleydev.jokeur_api.exceptions.owner;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OwnerEmailAlreadyUsedException extends RuntimeException {

  public OwnerEmailAlreadyUsedException(String email) {
    super("Email already used: " + email);
  }
}
