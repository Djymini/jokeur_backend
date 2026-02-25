package com.ashleydev.jokeur_api.exposition.dtos.vaccine;

import com.ashleydev.jokeur_api.exposition.dtos.ReminderResponseDto;
import java.time.LocalDate;

public record VaccineResponseDto(Long id, String name, String description, LocalDate vaccinDate, Long healthRecordId, ReminderResponseDto reminder) {}
