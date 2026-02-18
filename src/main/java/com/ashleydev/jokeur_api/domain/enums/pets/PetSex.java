package com.ashleydev.jokeur_api.domain.enums.pets;

import lombok.Getter;
import org.aspectj.apache.bcel.classfile.Unknown;

@Getter
public enum PetSex {
    MALE("Mâle"),
    FEMALE("Femelle"),
    UNKNOWN("Inconnu");

    private final String label;
    PetSex(String label) { this.label = label; }
}
