package com.ashleydev.jokeur_api.mappers;

import com.ashleydev.jokeur_api.exposition.dtos.appointment.AppointmentResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.AppointmentEntity;

public class AppointmentMapper {

  public static AppointmentResponseDto toDto(AppointmentEntity entity) {
    return new AppointmentResponseDto(entity.getId(), entity.getReason(), entity.getDateTime());
  }
}
