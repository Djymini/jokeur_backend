package com.ashleydev.jokeur_api.exposition.dtos.healthRecord;

import com.ashleydev.jokeur_api.exposition.dtos.measure.HealthRecordMeasuresResponseDTO;
import java.math.BigDecimal;
import java.time.LocalDate;

public record HealthRecordResponseDto(
  Long id,
  Long userId,
  String petName,
  String breed,
  String sex,
  LocalDate birthDate,
  BigDecimal currentWeight,
  String color,
  String identificationNumber,
  String tattoo,
  String allergy,
  byte[] image,
  String imageType,
  String AnimalType,
  HealthRecordMeasuresResponseDTO measures
) {}
