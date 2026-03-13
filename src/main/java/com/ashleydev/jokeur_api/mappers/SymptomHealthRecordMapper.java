package com.ashleydev.jokeur_api.mappers;

import com.ashleydev.jokeur_api.exposition.dtos.symptom.SymptomResponseDTO;
import com.ashleydev.jokeur_api.exposition.dtos.symptomhealthrecord.AddSymptomToHealthRecordRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.symptomhealthrecord.SymptomHealthRecordDTO;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.SymptomEntity;
import com.ashleydev.jokeur_api.persistence.entities.SymptomHealthRecordEntity;

public class SymptomHealthRecordMapper {

  public static SymptomHealthRecordEntity toEntity(SymptomEntity symptom, HealthRecordEntity healthRecord, AddSymptomToHealthRecordRequestDTO dto) {
    SymptomHealthRecordEntity entity = new SymptomHealthRecordEntity();
    entity.setSymptom(symptom);
    entity.setHealthRecord(healthRecord);
    entity.setObservationDate(dto.observationDate());
    entity.setObservation(dto.observation());

    return entity;
  }

  public static SymptomHealthRecordDTO toDto(SymptomHealthRecordEntity entity) {
    if (entity == null) return null;

    SymptomEntity symptom = entity.getSymptom();

    return new SymptomHealthRecordDTO(
      entity.getId(),
      symptom != null ? new SymptomResponseDTO(symptom.getId(), symptom.getName()) : null,
      symptom != null ? symptom.getName() : null,
      entity.getObservationDate(),
      entity.getEndDate(),
      entity.getObservation(),
      entity.isActive()
    );
  }
}
