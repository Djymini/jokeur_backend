package com.ashleydev.jokeur_api.exposition.dtos.vaccine;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record VaccineRequestDto(
  String name,
  String description,
  String vaccinator,
  LocalDate vaccineDate,
  LocalDateTime vaccineReminderDate,
  Long healthRecordId
) {}
