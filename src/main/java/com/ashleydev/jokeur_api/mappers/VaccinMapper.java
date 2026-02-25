package com.ashleydev.jokeur_api.mappers;

import com.ashleydev.jokeur_api.exposition.dtos.vaccin.VaccinRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.vaccin.VaccinResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.ReminderEntity;
import com.ashleydev.jokeur_api.persistence.entities.VaccineEntity;

public class VaccinMapper {

  public static VaccineEntity toEntity(VaccinRequestDto dto, HealthRecordEntity healthRecordh, ReminderEntity reminderEntity) {
    VaccineEntity entity = new VaccineEntity();
    entity.setName(dto.name());
    entity.setDescription(dto.description());
    entity.setVaccineDate(dto.vaccinDate());
    entity.setHealthRecordEntity(healthRecordh);
    entity.setReminderEntity(reminderEntity);

    return entity;
  }

  public static VaccinResponseDto toDto(VaccineEntity entity) {
    return new VaccinResponseDto(
      entity.getId(),
      entity.getName(),
      entity.getDescription(),
      entity.getVaccineDate(),
      entity.getHealthRecordEntity().getId(),
      ReminderMapper.toDto(entity.getReminderEntity())
    );
  }
}
