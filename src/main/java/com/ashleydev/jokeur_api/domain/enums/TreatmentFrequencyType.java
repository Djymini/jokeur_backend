package com.ashleydev.jokeur_api.domain.enums;

import lombok.Getter;

@Getter
public enum TreatmentFrequencyType {
  DAILY("Journalier"),
  WEEKLY("Hebdomadaire"),
  MONTHLY("Mensuel"),
  ANNUAL("Annuel"),
  ONETIME("Prise unique");

  private final String label;

  TreatmentFrequencyType(String label) {
    this.label = label;
  }
}
