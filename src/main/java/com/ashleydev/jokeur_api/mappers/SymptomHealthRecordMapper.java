package com.ashleydev.jokeur_api.mappers;

import com.ashleydev.jokeur_api.exposition.dtos.symptomhealthrecord.AddSymptomToHealthRecordDTO;
import com.ashleydev.jokeur_api.exposition.dtos.symptomhealthrecord.SymptomHealthRecordDTO;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.SymptomEntity;
import com.ashleydev.jokeur_api.persistence.entities.SymptomHealthRecordEntity;

public class SymptomHealthRecordMapper {


    public static SymptomHealthRecordEntity toEntity(
            SymptomEntity symptom,
            HealthRecordEntity healthRecord,
            AddSymptomToHealthRecordDTO dto
    ) {

        SymptomHealthRecordEntity entity = new SymptomHealthRecordEntity();
        entity.setSymptom(symptom);
        entity.setHealthRecord(healthRecord);
        entity.setStartDate(dto.startDate());
        entity.setEndDate(dto.endDate());
        entity.setObservation(dto.observation());

        return entity;
    }

    public static SymptomHealthRecordDTO toDto(SymptomHealthRecordEntity entity) {

        if (entity == null) return null;

        SymptomEntity symptom = entity.getSymptom();

        return new SymptomHealthRecordDTO(
                entity.getId(),
                symptom != null ? symptom.getId() : null,
                symptom != null ? symptom.getName() : null,
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getObservation(),
                entity.isActive()

        );
    }
}
