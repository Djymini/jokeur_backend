package com.ashleydev.jokeur_api.exposition.dtos.agenda;

import com.ashleydev.jokeur_api.exposition.dtos.appointment.AppointmentResponseDto;
import com.ashleydev.jokeur_api.exposition.dtos.reminder.ReminderResponseDto;
import java.util.List;

public record AgendaResponseDto(List<ReminderResponseDto> reminderList, List<AppointmentResponseDto> appointmentList) {}
