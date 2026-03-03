package com.ashleydev.jokeur_api.exposition.dtos.vaccine;

import com.ashleydev.jokeur_api.exposition.dtos.reminder.ReminderVaccineRequestDto;
import java.time.LocalDate;

public record VaccineDetailRequestDto(
  Long id,
  String name,
  String description,
  String vaccinator,
  LocalDate vaccineDate,
  Long healthRecordId,
  ReminderVaccineRequestDto reminder
) {}
