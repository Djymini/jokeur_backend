package com.ashleydev.jokeur_api.domain.enums.pets;

import lombok.Getter;

@Getter
public enum AnimalType {
  DOG("Chien"),
  CAT("Chat");

  private final String label;

  AnimalType(String label) {
    this.label = label;
  }
}
