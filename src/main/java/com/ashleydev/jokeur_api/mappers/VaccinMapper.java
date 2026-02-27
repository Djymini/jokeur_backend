package com.ashleydev.jokeur_api.mappers;

import com.ashleydev.jokeur_api.exposition.dtos.vaccine.VaccineDetailRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.vaccine.VaccineRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.vaccine.VaccineResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.ReminderEntity;
import com.ashleydev.jokeur_api.persistence.entities.VaccineEntity;

public class VaccinMapper {

  public static VaccineEntity toEntity(VaccineRequestDto dto, HealthRecordEntity healthRecord, ReminderEntity reminderEntity) {
    VaccineEntity entity = new VaccineEntity();
    entity.setName(dto.name());
    entity.setDescription(dto.description());
    entity.setVaccinator(dto.vaccinator());
    entity.setVaccineDate(dto.vaccineDate());
    entity.setHealthRecordEntity(healthRecord);
    entity.setReminderEntity(reminderEntity);

    return entity;
  }

  public static VaccineEntity toEntity(VaccineDetailRequestDto dto, HealthRecordEntity healthRecord, ReminderEntity reminderEntity) {
    VaccineEntity entity = new VaccineEntity();
    entity.setId(dto.id());
    entity.setName(dto.name());
    entity.setDescription(dto.description());
    entity.setVaccinator(dto.vaccinator());
    entity.setVaccineDate(dto.vaccineDate());
    entity.setHealthRecordEntity(healthRecord);
    entity.setReminderEntity(reminderEntity);

    return entity;
  }

  public static VaccineResponseDto toDto(VaccineEntity entity) {
    return new VaccineResponseDto(
      entity.getId(),
      entity.getName(),
      entity.getDescription(),
      entity.getVaccinator(),
      entity.getVaccineDate(),
      entity.getHealthRecordEntity().getId(),
      ReminderMapper.toDto(entity.getReminderEntity())
    );
  }
}
