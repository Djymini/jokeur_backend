package com.ashleydev.jokeur_api.exceptions.owner;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OwnerNotFoundException extends RuntimeException {

  public OwnerNotFoundException(Long ownerId) {
    super("Owner not found with id: " + ownerId);
  }

  public OwnerNotFoundException(String email) {
    super("Owner not found with email: " + email);
  }
}
