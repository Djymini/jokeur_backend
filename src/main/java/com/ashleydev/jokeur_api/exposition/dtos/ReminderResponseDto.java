package com.ashleydev.jokeur_api.exposition.dtos;

import com.ashleydev.jokeur_api.domain.ReminderStatus;
import com.ashleydev.jokeur_api.domain.enums.ReminderType;

import java.time.LocalDate;

public record ReminderResponseDto(
        Long id,
        ReminderType type,
        String description,
        LocalDate reminderDate,
        Boolean notificationSent,
        ReminderStatus status
) {}
