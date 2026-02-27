package com.ashleydev.jokeur_api.mappers;

import com.ashleydev.jokeur_api.exposition.dtos.reminder.ReminderResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.ReminderEntity;

public class ReminderMapper {

  public static ReminderResponseDto toDto(ReminderEntity entity) {
    return new ReminderResponseDto(entity.getId(), entity.getType(), entity.getDescription(), entity.getReminderDate(), entity.getStatus());
  }
}
