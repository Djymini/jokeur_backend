package com.ashleydev.jokeur_api.exceptions.appointment;

public class AppointmentDeleteFailedException extends RuntimeException {

  public AppointmentDeleteFailedException(String message) {
    super(message);
  }
}
