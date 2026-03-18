package com.ashleydev.jokeur_api.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserEmailAlreadyUsedException extends RuntimeException {

  public UserEmailAlreadyUsedException(String email) {
    super("Cet email est déjà utilisé : " + email);
  }
}
