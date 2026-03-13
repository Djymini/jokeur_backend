package com.ashleydev.jokeur_api.mappers;

import com.ashleydev.jokeur_api.exposition.dtos.treatment.TreatmentDetailRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.treatment.TreatmentRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.treatment.TreatmentResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.ReminderEntity;
import com.ashleydev.jokeur_api.persistence.entities.TreatmentEntity;

public class TreatmentMapper {

  public static TreatmentEntity toEntity(TreatmentRequestDto dto, HealthRecordEntity healthRecord, ReminderEntity reminderEntity) {
    TreatmentEntity entity = new TreatmentEntity();
    entity.setName(dto.name());
    entity.setDescription(dto.description());
    entity.setFrequency(dto.frequency());
    entity.setBeginDate(dto.beginDate());
    entity.setEndDate(dto.endDate());
    entity.setHealthRecordEntity(healthRecord);
    entity.setReminderEntity(reminderEntity);

    return entity;
  }

  public static TreatmentEntity toEntity(TreatmentDetailRequestDto dto, HealthRecordEntity healthRecord, ReminderEntity reminderEntity) {
    TreatmentEntity entity = new TreatmentEntity();
    entity.setId(dto.id());
    entity.setName(dto.name());
    entity.setDescription(dto.description());
    entity.setFrequency(dto.frequency());
    entity.setBeginDate(dto.beginDate());
    entity.setEndDate(dto.endDate());
    entity.setHealthRecordEntity(healthRecord);
    entity.setReminderEntity(reminderEntity);

    return entity;
  }

  public static TreatmentResponseDto toDto(TreatmentEntity entity) {
    return new TreatmentResponseDto(
      entity.getId(),
      entity.getName(),
      entity.getDescription(),
      entity.getFrequency(),
      entity.getBeginDate(),
      entity.getEndDate(),
      entity.getHealthRecordEntity().getId(),
      ReminderMapper.toDto(entity.getReminderEntity())
    );
  }
}
