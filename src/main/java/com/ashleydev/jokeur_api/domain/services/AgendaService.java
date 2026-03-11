package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.annotations.ValidateUser;
import com.ashleydev.jokeur_api.domain.rules.AgendaRules;
import com.ashleydev.jokeur_api.exposition.dtos.agenda.AgendaResponseDto;
import com.ashleydev.jokeur_api.mappers.AppointmentMapper;
import com.ashleydev.jokeur_api.mappers.ReminderMapper;
import com.ashleydev.jokeur_api.persistence.repositories.AppoinmentRepository;
import com.ashleydev.jokeur_api.persistence.repositories.ReminderRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgendaService {

  @Autowired
  private ReminderRepository reminderRepository;

  @Autowired
  private AppoinmentRepository appointntmentRepository;

  @ValidateUser
  public AgendaResponseDto getDateDuringPeriod(Long userId, LocalDateTime date) {
    LocalDateTime startDate = AgendaRules.getFirstDayOfMonth(date);
    LocalDateTime endDate = AgendaRules.getLastDayOfMonth(date);

    return new AgendaResponseDto(
      reminderRepository.findPendingReminderDuringPeriod(userId, startDate, endDate).stream().map(ReminderMapper::toDto).toList(),
      appointntmentRepository.findPendingAppointmentDuringPeriod(userId, startDate, endDate).stream().map(AppointmentMapper::toDto).toList()
    );
  }
}
