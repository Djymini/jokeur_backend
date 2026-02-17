package com.ashleydev.jokeur_api.exceptions.healthRecord;

public class HealthRecordNotFoundException extends RuntimeException {

  public HealthRecordNotFoundException(Long id) {
    super("Health record not found with id: " + id);
  }
}
