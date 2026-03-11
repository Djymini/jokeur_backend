package com.ashleydev.jokeur_api.domain.rules;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

public class AgendaRules {

  public static LocalDate getFirstDayOfMonth(LocalDateTime date) {
    Month month = date.getMonth();
    int year = date.getYear();

    return LocalDate.of(year, month, 1);
  }

  public static LocalDate getLastDayOfMonth(LocalDateTime date) {
    Month month = date.getMonth();
    int year = date.getYear();
    int day;

    switch (month) {
      case JANUARY, MARCH, MAY, JULY, AUGUST, OCTOBER, DECEMBER -> day = 31;
      case FEBRUARY -> day = 28;
      default -> day = 30;
    }

    return LocalDate.of(year, month, day);
  }
}
