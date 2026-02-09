package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.exposition.dtos.ReminderResponseDto;
import com.ashleydev.jokeur_api.mappers.ReminderMapper;
import com.ashleydev.jokeur_api.persistence.repositories.ReminderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private static final int REMINDER_MONTH_INTERVAL = 3;

    public List<ReminderResponseDto> getReminders(Long idOwner){
        LocalDate maxIntervalDate = LocalDate.now().plusMonths(REMINDER_MONTH_INTERVAL);
       return reminderRepository.findByOwnerId(idOwner, maxIntervalDate).stream().map(ReminderMapper::toDto).toList();
    }
}
