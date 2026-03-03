package com.ashleydev.jokeur_api.exceptions.healthRecord;

public class HealthRecordUpdateEmptyException extends RuntimeException {

  public HealthRecordUpdateEmptyException() {
    super("At least one field must be provided to update a health record.");
  }
}
