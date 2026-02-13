package com.ashleydev.jokeur_api.mappers;

import com.ashleydev.jokeur_api.exposition.dtos.HealthRecordResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;

public class HealthRecordMapper {

    public static HealthRecordResponseDto toDto(HealthRecordEntity entity){
        return new HealthRecordResponseDto(
                entity.getId(),
                entity.getPetName(),
                entity.getBreed(),
                entity.getSex(),
                entity.getBirthDate(),
                entity.getCurrentWeight(),
                entity.getColor(),
                entity.getIdentificationNumber(),
                entity.getTattoo(),
                entity.getAllergy(),
                entity.getImage(),
                entity.getImageType(),
                entity.getAnimalType()
        );
    }
}
