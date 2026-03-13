package com.ashleydev.jokeur_api.domain.rules;

import java.time.LocalDateTime;
import java.time.Month;

public class AgendaRules {

  public static LocalDateTime getFirstDayOfMonth(LocalDateTime date) {
    Month month = date.getMonth();
    int year = date.getYear();

    return LocalDateTime.of(year, month, 1, 0, 0);
  }

  public static LocalDateTime getLastDayOfMonth(LocalDateTime date) {
    Month month = date.getMonth();
    int year = date.getYear();
    int day;

    switch (month) {
      case JANUARY, MARCH, MAY, JULY, AUGUST, OCTOBER, DECEMBER -> day = 31;
      case FEBRUARY -> day = 28;
      default -> day = 30;
    }

    return LocalDateTime.of(year, month, day, 23, 59);
  }
}
