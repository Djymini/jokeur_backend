package com.ashleydev.jokeur_api.mappers;

import com.ashleydev.jokeur_api.exposition.dtos.appointment.AppointmentRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.appointment.AppointmentResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.*;

public class AppointmentMapper {

  public static AppointmentResponseDto toDto(AppointmentEntity entity) {
    return new AppointmentResponseDto(entity.getId(), entity.getReason(), entity.getDateTime(), entity.getDuration());
  }

  public static AppointmentEntity toEntity(AppointmentRequestDto dto, UserEntity user) {
    AppointmentEntity entity = new AppointmentEntity();
    entity.setReason(dto.reason());
    entity.setDateTime(dto.dateTime());
    entity.setDuration(dto.duration());
    entity.setUser(user);

    return entity;
  }
}
