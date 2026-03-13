package com.ashleydev.jokeur_api.exposition.dtos.treatment;

import com.ashleydev.jokeur_api.domain.enums.TreatmentFrequencyType;
import com.ashleydev.jokeur_api.exposition.dtos.reminder.ReminderVaccineRequestDto;
import java.time.LocalDate;

public record TreatmentDetailRequestDto(
  Long id,
  String name,
  String description,
  TreatmentFrequencyType frequency,
  LocalDate beginDate,
  LocalDate endDate,
  Long healthRecordId,
  ReminderVaccineRequestDto reminder
) {}
