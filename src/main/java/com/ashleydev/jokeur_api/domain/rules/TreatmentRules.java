package com.ashleydev.jokeur_api.domain.rules;

public class TreatmentRules {

  public static String formatReminderTreatmentDescription(String treatmentName, String petName) {
    return "Rappel pour le traitement : " + treatmentName + " de " + petName;
  }
}
