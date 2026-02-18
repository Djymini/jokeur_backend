package com.ashleydev.jokeur_api.domain.enums.pets;

import lombok.Getter;

@Getter
public enum PetSex {
    MALE("Mâle"),
    FEMALE("Femelle"),
    UNKNOWN("Inconnu");

    private final String label;
    PetSex(String label) { this.label = label; }
}
