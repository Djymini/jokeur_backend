package com.ashleydev.jokeur_api.exposition.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HealthRecordResponseDto(
        Long id,
        String petName,
        String breed,
        String sex,
        LocalDate birthDate,
        BigDecimal currentWeight,
        String color,
        String identificationNumber,
        String tattoo,
        String allergy,
        Enum AnimalType

) {}
