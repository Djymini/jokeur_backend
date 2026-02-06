package com.ashleydev.jokeur_api.exposition.dtos.healthRecord;

import com.ashleydev.jokeur_api.domain.enums.AnimalType;
import com.ashleydev.jokeur_api.domain.enums.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.PetColor;
import com.ashleydev.jokeur_api.domain.enums.PetSex;
import java.math.BigDecimal;
import java.time.LocalDate;

public record HealthRecordResponseDTO(
  Long healthRecordNumber,
  Long ownerId,
  String petName,
  AnimalType animalType,
  PetBreed breed,
  PetSex sex,
  LocalDate birthDate,
  BigDecimal currentWeight,
  PetColor color,
  String identificationNumber,
  String tattooNumber,
  String allergy
) {}
