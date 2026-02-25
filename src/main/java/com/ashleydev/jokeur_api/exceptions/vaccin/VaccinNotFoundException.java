package com.ashleydev.jokeur_api.exceptions.vaccin;

public class VaccinNotFoundException extends RuntimeException {

  public VaccinNotFoundException(Long id) {
    super("Le vaccin avec l'id : " + id + " n'existe pas");
  }
}
