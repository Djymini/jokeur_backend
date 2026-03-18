package com.ashleydev.jokeur_api.exposition.dtos.treatment;

import com.ashleydev.jokeur_api.exposition.dtos.reminder.ReminderResponseDto;
import java.time.LocalDate;

public record TreatmentResponseDto(
  Long id,
  String name,
  String description,
  String frequency,
  LocalDate beginDate,
  LocalDate endDate,
  Long healthRecordId,
  ReminderResponseDto reminder
) {}
