package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.exposition.dtos.appointment.AppointmentResponseDto;
import com.ashleydev.jokeur_api.mappers.AppointmentMapper;
import com.ashleydev.jokeur_api.persistence.repositories.AppoinmentRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppointmentService {

  private final AppoinmentRepository appoinmentRepository;

  public List<AppointmentResponseDto> getAllAppointement(Long idOwner) {
    return appoinmentRepository.getAllAppointement(idOwner).stream().map(AppointmentMapper::toDto).toList();
  }
}
