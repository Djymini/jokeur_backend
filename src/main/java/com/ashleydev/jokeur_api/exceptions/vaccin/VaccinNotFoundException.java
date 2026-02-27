package com.ashleydev.jokeur_api.exceptions.vaccin;

public class VaccinNotFoundException extends RuntimeException {

  public VaccinNotFoundException(Long id) {
    super("Vaccine not found with id: " + id);
  }
}
