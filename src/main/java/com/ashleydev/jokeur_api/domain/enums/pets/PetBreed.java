package com.ashleydev.jokeur_api.domain.enums.pets;

import lombok.Getter;

@Getter
public enum PetBreed {
  LABRADOR("Labrador", AnimalType.DOG),
  GERMAN_SHEPHERD("Berger allemand", AnimalType.DOG),
  GOLDEN_RETRIEVER("Golden Retriever", AnimalType.DOG),
  BULLDOG("Bulldog", AnimalType.DOG),
  MIXED("Croisé", AnimalType.DOG),
  SHIH_TZU("Shih Tzu", AnimalType.CAT),
  PERSIAN("Persan", AnimalType.CAT),
  EUROPEAN("Européen", AnimalType.CAT),
  MAINE_COON("Maine Coon", AnimalType.CAT),
  BRITISH_SHORTHAIR("British Shorthair", AnimalType.CAT),
  SIAMESE("Siamois", AnimalType.CAT),
  UNKNOWN("Inconnu", null);

  private final String label;
  private final AnimalType animalType;

  PetBreed(String label, AnimalType animalType) {
    this.label = label;
    this.animalType = animalType;
  }
}
