package com.ashleydev.jokeur_api.exposition.dtos.vaccin;

import com.ashleydev.jokeur_api.exposition.dtos.ReminderResponseDto;
import java.time.LocalDate;

public record VaccinResponseDto(Long id, String name, String description, LocalDate vaccinDate, Long healthRecordId, ReminderResponseDto reminder) {}
