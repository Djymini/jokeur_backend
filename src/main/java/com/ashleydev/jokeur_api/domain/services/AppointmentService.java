package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.annotations.ValidateAppointment;
import com.ashleydev.jokeur_api.annotations.ValidateUser;
import com.ashleydev.jokeur_api.exceptions.appointment.AppointmentDeleteFailedException;
import com.ashleydev.jokeur_api.exceptions.appointment.AppointmentNotFoundException;
import com.ashleydev.jokeur_api.exposition.dtos.appointment.AppointmentRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.appointment.AppointmentResponseDto;
import com.ashleydev.jokeur_api.mappers.AppointmentMapper;
import com.ashleydev.jokeur_api.persistence.entities.*;
import com.ashleydev.jokeur_api.persistence.repositories.AppoinmentRepository;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppointmentService {

  private final AppoinmentRepository appoinmentRepository;

  private final UserRepository userRepository;

  @ValidateUser
  public Page<AppointmentResponseDto> getAllAppointement(Long userId, Pageable pageable) {
    return appoinmentRepository.getAllAppointement(userId, pageable).map(AppointmentMapper::toDto);
  }

  public AppointmentResponseDto getById(Long id) {
    if (!appoinmentRepository.existsById(id)) throw new AppointmentNotFoundException(id);
    AppointmentEntity appointmentEntity = appoinmentRepository.findById(id).get();

    return AppointmentMapper.toDto(appointmentEntity);
  }

  @ValidateUser
  public AppointmentResponseDto create(AppointmentRequestDto request) {
    UserEntity user = userRepository.findById(request.userId()).get();
    AppointmentEntity newAppointment = appoinmentRepository.save(AppointmentMapper.toEntity(request, user));
    return AppointmentMapper.toDto(newAppointment);
  }

  @ValidateUser
  @ValidateAppointment
  public AppointmentResponseDto update(AppointmentRequestDto request, Long id) {
    UserEntity user = userRepository.findById(request.userId()).get();

    AppointmentEntity updateEntity = AppointmentMapper.toEntity(request, user);
    updateEntity.setId(id);

    AppointmentEntity response = appoinmentRepository.save(updateEntity);

    return AppointmentMapper.toDto(response);
  }

  @ValidateUser
  @ValidateAppointment
  public String delete(Long id, Long userId) {
    appoinmentRepository.deleteById(id);

    if (appoinmentRepository.existsById(id)) {
      throw new AppointmentDeleteFailedException("Appointment : " + id + " is not deleted");
    }

    return "Appointment : " + id + " is deleted";
  }
}
