package com.ashleydev.jokeur_api.exposition.dtos.vaccine;

import java.time.LocalDate;

public record VaccineRequestDto(
  String name,
  String description,
  String vaccinator,
  LocalDate vaccineDate,
  LocalDate vaccineReminderDate,
  Long healthRecordId
) {}
