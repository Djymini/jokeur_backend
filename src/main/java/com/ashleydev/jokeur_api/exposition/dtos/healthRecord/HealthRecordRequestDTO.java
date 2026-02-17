package com.ashleydev.jokeur_api.exposition.dtos.healthRecord;

import com.ashleydev.jokeur_api.domain.enums.AnimalType;
import com.ashleydev.jokeur_api.domain.enums.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.PetColor;
import com.ashleydev.jokeur_api.domain.enums.PetSex;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record HealthRecordRequestDTO(
  @NotNull(message = "ownerId is required") Long ownerId,

  @NotBlank(message = "petName is required") @Size(max = 50, message = "petName must not exceed 50 characters") String petName,

  @NotNull(message = "animalType is required") AnimalType animalType,

  PetBreed breed,

  @NotNull(message = "sex is required") PetSex sex,

  @PastOrPresent(message = "birthDate cannot be in the future") LocalDate birthDate,

  @DecimalMin(value = "0.01", message = "currentWeight must be > 0")
  @Digits(integer = 2, fraction = 2, message = "currentWeight format is invalid (max 2 digits + 2 decimals)")
  BigDecimal currentWeight,

  PetColor color,

  @Size(max = 20, message = "identificationNumber must not exceed 20 characters") String id,

  @Size(max = 50, message = "tattooNumber must not exceed 50 characters") String tattooNumber,

  @Size(max = 100, message = "allergy must not exceed 100 characters") String allergy
) {}
