package com.ashleydev.jokeur_api.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserPseudoAlreadyUsedException extends RuntimeException {

  public UserPseudoAlreadyUsedException(String pseudo) {
    super("Pseudo already used: " + pseudo);
  }
}
