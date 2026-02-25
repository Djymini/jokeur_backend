package com.ashleydev.jokeur_api.exposition.dtos.vaccine;

import com.ashleydev.jokeur_api.exposition.dtos.reminder.ReminderResponseDto;
import java.time.LocalDate;

public record VaccineResponseDto(Long id, String name, String description, String vaccinator, LocalDate vaccinDate, Long healthRecordId, ReminderResponseDto reminder) {}
