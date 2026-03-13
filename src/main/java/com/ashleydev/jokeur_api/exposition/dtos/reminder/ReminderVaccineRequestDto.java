package com.ashleydev.jokeur_api.exposition.dtos.reminder;

import com.ashleydev.jokeur_api.domain.enums.ReminderStatus;
import java.time.LocalDateTime;

public record ReminderVaccineRequestDto(Long id, String description, LocalDateTime reminderDate, ReminderStatus status) {}
