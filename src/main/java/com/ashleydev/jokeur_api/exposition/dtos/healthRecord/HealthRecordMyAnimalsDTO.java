package com.ashleydev.jokeur_api.exposition.dtos.healthRecord;

import com.ashleydev.jokeur_api.domain.enums.pets.AnimalType;
import com.ashleydev.jokeur_api.domain.enums.pets.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.pets.PetSex;
import java.math.BigDecimal;

public record HealthRecordMyAnimalsDTO(Long id, String petName, AnimalType animalType, PetBreed breed, PetSex sex, BigDecimal currentWeight) {}
