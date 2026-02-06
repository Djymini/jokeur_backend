package com.ashleydev.jokeur_api.exceptions.owner;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OwnerUpdateEmptyException extends RuntimeException {

  public OwnerUpdateEmptyException() {
    super("No fields provided for update.");
  }
}
