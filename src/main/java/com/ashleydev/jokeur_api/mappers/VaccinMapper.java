package com.ashleydev.jokeur_api.mappers;

import com.ashleydev.jokeur_api.exposition.dtos.vaccin.VaccinRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.vaccin.VaccinResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.ReminderEntity;
import com.ashleydev.jokeur_api.persistence.entities.VaccinEntity;

public class VaccinMapper {

  public static VaccinEntity toEntity(VaccinRequestDto dto, HealthRecordEntity healthRecordh, ReminderEntity reminderEntity) {
    VaccinEntity entity = new VaccinEntity();
    entity.setName(dto.name());
    entity.setDescription(dto.description());
    entity.setVaccinDate(dto.vaccinDate());
    entity.setHealthRecordEntity(healthRecordh);
    entity.setReminderEntity(reminderEntity);

    return entity;
  }

  public static VaccinResponseDto toDto(VaccinEntity entity) {
    return new VaccinResponseDto(
      entity.getId(),
      entity.getName(),
      entity.getDescription(),
      entity.getVaccinDate(),
      entity.getHealthRecordEntity().getId(),
      ReminderMapper.toDto(entity.getReminderEntity())
    );
  }
}
