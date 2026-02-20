package com.ashleydev.jokeur_api.exceptions.reminder;

public class ReminderNotFoundException extends RuntimeException {

  public ReminderNotFoundException(String message) {
    super(message);
  }
}
