package com.ashleydev.jokeur_api.exceptions.healthRecord;

public class IdentificationNumberAlreadyUsedException extends RuntimeException {

  public IdentificationNumberAlreadyUsedException(String identificationNumber) {
    super("Identification number '" + identificationNumber + "' is already in use.");
  }
}
