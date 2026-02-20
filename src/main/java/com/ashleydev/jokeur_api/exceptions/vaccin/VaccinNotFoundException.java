package com.ashleydev.jokeur_api.exceptions.vaccin;

public class VaccinNotFoundException extends RuntimeException {

  public VaccinNotFoundException(String message) {
    super(message);
  }
}
