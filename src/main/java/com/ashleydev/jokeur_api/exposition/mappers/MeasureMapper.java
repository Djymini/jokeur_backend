package com.ashleydev.jokeur_api.exposition.mappers;

import com.ashleydev.jokeur_api.domain.enums.MeasureType;
import com.ashleydev.jokeur_api.exposition.dtos.measure.MeasureRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.measure.MeasureResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.MeasureEntity;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;

public class MeasureMapper {

  private HealthRecordRepository healthRecordRepository;

  public static MeasureEntity toEntity(MeasureRequestDto dto, HealthRecordEntity healthRecordEntity) {
    MeasureEntity entity = new MeasureEntity();
    entity.setMeasureValue(dto.value());
    entity.setMeasureType(MeasureType.valueOf(dto.measureType().toUpperCase()));
    entity.setHealthRecordEntity(healthRecordEntity);

    return entity;
  }

  public static MeasureResponseDto toDto(MeasureEntity entity) {
    return new MeasureResponseDto(
      entity.getIdMeasure(),
      entity.getMeasureValue(),
      entity.getMeasureType().toString(),
      entity.getHealthRecordEntity().getHealthRecordNumber(),
      entity.getCreationDate()
    );
  }
}
