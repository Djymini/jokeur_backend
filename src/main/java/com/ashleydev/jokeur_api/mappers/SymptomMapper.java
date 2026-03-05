package com.ashleydev.jokeur_api.mappers;

import com.ashleydev.jokeur_api.exposition.dtos.symptom.SymptomResponseDTO;
import com.ashleydev.jokeur_api.persistence.entities.SymptomEntity;

public class SymptomMapper {

    public static SymptomResponseDTO toDto(SymptomEntity entity) {

        if (entity == null) return null;

        return new SymptomResponseDTO(
                entity.getId(),
                entity.getName()
        );
    }


}