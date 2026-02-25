package com.ashleydev.jokeur_api.exposition.dtos.vaccine;

import java.time.LocalDate;

public record VaccineRequestDto(String name, String description, LocalDate vaccinDate, LocalDate vaccinReminderDate, Long healthRecordId) {}
