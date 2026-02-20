package com.ashleydev.jokeur_api.exposition.dtos.vaccin;

import java.time.LocalDate;

public record VaccinRequestDto(String name, String description, LocalDate vaccinDate, LocalDate vaccinReminderDate, Long healthRecordId) {}
