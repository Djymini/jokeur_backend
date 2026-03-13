package com.ashleydev.jokeur_api.exposition.dtos.treatment;

import com.ashleydev.jokeur_api.domain.enums.TreatmentFrequencyType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TreatmentRequestDto(
  String name,
  String description,
  TreatmentFrequencyType frequency,
  LocalDate beginDate,
  LocalDate endDate,
  LocalDateTime treatmentReminderDate,
  Long healthRecordId
) {}
