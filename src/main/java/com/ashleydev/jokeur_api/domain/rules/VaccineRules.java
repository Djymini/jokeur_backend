package com.ashleydev.jokeur_api.domain.rules;

public class VaccineRules {

  public static String formatReminderVaccineDescription(String vaccineName, String petName) {
    return "Rappel pour le vaccin : " + vaccineName + " de " + petName;
  }
}
