package com.ashleydev.jokeur_api.mappers;

import com.ashleydev.jokeur_api.domain.enums.MeasureType;
import com.ashleydev.jokeur_api.exposition.dtos.measure.HealthRecordMeasuresResponseDTO;
import com.ashleydev.jokeur_api.exposition.dtos.measure.MeasureRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.measure.MeasureResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.MeasureEntity;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import java.util.ArrayList;
import java.util.List;

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
      entity.getId(),
      entity.getMeasureValue(),
      entity.getMeasureType().toString(),
      entity.getHealthRecordEntity().getId(),
      entity.getCreationDate()
    );
  }

  public static HealthRecordMeasuresResponseDTO toHealthRecordDto(List<MeasureEntity> allEntity) {
    List<MeasureResponseDto> weight = createMeasureResponseDtoList(allEntity, MeasureType.WEIGHT);
    List<MeasureResponseDto> temperature = createMeasureResponseDtoList(allEntity, MeasureType.TEMPERATURE);
    List<MeasureResponseDto> respiratoryRate = createMeasureResponseDtoList(allEntity, MeasureType.RESPIRATORY_RATE);
    List<MeasureResponseDto> bpm = createMeasureResponseDtoList(allEntity, MeasureType.BPM);

    return new HealthRecordMeasuresResponseDTO(temperature, weight, respiratoryRate, bpm);
  }

  private static List<MeasureResponseDto> createMeasureResponseDtoList(List<MeasureEntity> entity, MeasureType type) {
    List<MeasureResponseDto> measureResponseDtoArrayList = new ArrayList<MeasureResponseDto>();
    for (MeasureEntity measure : entity) {
      if (measure.getMeasureType() == type) {
        measureResponseDtoArrayList.add(toDto(measure));
      }
    }

    return measureResponseDtoArrayList;
  }
}
