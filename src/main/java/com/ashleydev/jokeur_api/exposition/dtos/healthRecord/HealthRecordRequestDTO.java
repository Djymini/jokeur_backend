package com.ashleydev.jokeur_api.exposition.dtos.healthRecord;

import com.ashleydev.jokeur_api.domain.enums.pets.AnimalType;
import com.ashleydev.jokeur_api.domain.enums.pets.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.pets.PetColor;
import com.ashleydev.jokeur_api.domain.enums.pets.PetSex;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record HealthRecordRequestDTO(
        @NotNull(message = "userId is required") Long userId,

        @NotBlank(message = "petName is required")
        @Size(min = 2, max = 50, message = "petName must be between 2 and 50 characters") String petName,

        @NotNull(message = "animalType is required") AnimalType animalType,

        @NotNull(message = "breed is required") PetBreed breed,

        @NotNull(message = "sex is required") PetSex sex,

        @PastOrPresent(message = "birthDate cannot be in the future") LocalDate birthDate,

        @DecimalMin(value = "0.01", message = "currentWeight must be > 0")
        @DecimalMax(value = "999.99", message = "currentWeight must be < 1000")
        @Digits(integer = 3, fraction = 2, message = "currentWeight format is invalid") BigDecimal currentWeight,

        @NotNull(message = "color is required") PetColor color,

        @Size(max = 20, message = "identificationNumber must not exceed 20 characters")
        @Pattern(regexp = "^[A-Za-z0-9]{2,20}$", message = "identificationNumber format is invalid") String identificationNumber,

        @Size(max = 50, message = "tattooNumber must not exceed 50 characters")
        @Pattern(regexp = "^[A-Za-z0-9]{2,10}$", message = "tattooNumber format is invalid") String tattoo,

        @Size(max = 100, message = "allergy must not exceed 100 characters") String allergy
) {}
