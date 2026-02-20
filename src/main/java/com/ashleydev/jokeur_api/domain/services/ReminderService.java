package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.exposition.dtos.ReminderResponseDto;
import com.ashleydev.jokeur_api.mappers.ReminderMapper;
import com.ashleydev.jokeur_api.persistence.repositories.ReminderRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReminderService {

  private static final int REMINDER_MONTH_INTERVAL = 3;

  private final ReminderRepository reminderRepository;

  public List<ReminderResponseDto> getReminders(Long userId) {
    LocalDate maxIntervalDate = LocalDate.now().plusMonths(REMINDER_MONTH_INTERVAL);
    return reminderRepository.findByUserId(userId, maxIntervalDate).stream().map(ReminderMapper::toDto).toList();
  }
}
