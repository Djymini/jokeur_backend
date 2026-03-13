package com.ashleydev.jokeur_api.exposition.dtos.reminder;

import com.ashleydev.jokeur_api.domain.enums.ReminderStatus;
import com.ashleydev.jokeur_api.domain.enums.ReminderType;
import java.time.LocalDateTime;

public record ReminderResponseDto(Long id, ReminderType type, String description, LocalDateTime reminderDate, ReminderStatus status) {}
