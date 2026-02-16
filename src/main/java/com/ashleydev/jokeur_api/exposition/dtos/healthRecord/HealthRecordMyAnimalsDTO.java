package com.ashleydev.jokeur_api.exposition.dtos.healthRecord;

import com.ashleydev.jokeur_api.domain.enums.AnimalType;
import com.ashleydev.jokeur_api.domain.enums.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.PetSex;
import java.math.BigDecimal;

public record HealthRecordMyAnimalsDTO(
  Long healthRecordId,
  String petName,
  AnimalType animalType,
  PetBreed breed,
  PetSex sex,
  BigDecimal currentWeight
) {}
