package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.exposition.dtos.appointment.AppointmentResponseDto;
import com.ashleydev.jokeur_api.mappers.AppointmentMapper;
import com.ashleydev.jokeur_api.persistence.repositories.AppoinmentRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppointmentService {

  private final AppoinmentRepository appoinmentRepository;

  public Page<AppointmentResponseDto> getAllAppointement(Long userId, Pageable pageable) {
    return appoinmentRepository.getAllAppointement(userId, pageable).map(AppointmentMapper::toDto);
  }
}
