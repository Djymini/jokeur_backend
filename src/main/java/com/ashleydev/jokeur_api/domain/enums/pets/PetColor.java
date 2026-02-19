package com.ashleydev.jokeur_api.domain.enums.pets;

import lombok.Getter;

@Getter
public enum PetColor {
  BLACK("Noir"),
  WHITE("Blanc"),
  BROWN("Marron"),
  GINGER("Roux"),
  GREY("Gris"),
  OTHER("Autre"),
  MIXED("Bicolore / Tricolore");

  private final String label;

  PetColor(String label) {
    this.label = label;
  }
}
