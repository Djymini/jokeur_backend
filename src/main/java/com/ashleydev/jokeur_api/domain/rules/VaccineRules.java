package com.ashleydev.jokeur_api.domain.rules;

public class VaccineRules {

  public static String formatReminderVaccineDescription(String vaccineName) {
    return "Rappel pour le vaccin : " + vaccineName;
  }
}
