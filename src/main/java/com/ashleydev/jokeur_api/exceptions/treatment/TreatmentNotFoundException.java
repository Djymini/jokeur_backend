package com.ashleydev.jokeur_api.exceptions.treatment;

public class TreatmentNotFoundException extends RuntimeException {

  public TreatmentNotFoundException(Long id) {
    super("Treatment not found with id: " + id);
  }
}
