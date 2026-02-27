package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.exposition.dtos.reminder.ReminderResponseDto;
import com.ashleydev.jokeur_api.mappers.ReminderMapper;
import com.ashleydev.jokeur_api.persistence.repositories.ReminderRepository;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReminderService {

  private static final int REMINDER_MONTH_INTERVAL = 3;

  private final ReminderRepository reminderRepository;

  public Page<ReminderResponseDto> getReminders(Long userId, Pageable pageable) {
    LocalDate maxIntervalDate = LocalDate.now().plusMonths(REMINDER_MONTH_INTERVAL);
    return reminderRepository.findPendingReminder(userId, maxIntervalDate, pageable).map(ReminderMapper::toDto);
  }
}
