package com.ashleydev.jokeur_api.exposition.dtos.reminder;

import com.ashleydev.jokeur_api.domain.enums.ReminderStatus;
import java.time.LocalDate;

public record ReminderVaccineRequestDto(Long id, String description, LocalDate reminderDate, ReminderStatus status) {}
