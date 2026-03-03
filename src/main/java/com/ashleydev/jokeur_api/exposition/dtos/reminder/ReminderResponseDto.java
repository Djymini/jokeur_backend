package com.ashleydev.jokeur_api.exposition.dtos.reminder;

import com.ashleydev.jokeur_api.domain.enums.ReminderStatus;
import com.ashleydev.jokeur_api.domain.enums.ReminderType;
import java.time.LocalDate;

public record ReminderResponseDto(Long id, ReminderType type, String description, LocalDate reminderDate, ReminderStatus status) {}
