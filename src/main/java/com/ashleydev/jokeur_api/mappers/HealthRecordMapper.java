package com.ashleydev.jokeur_api.mappers;

import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordDashboardDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordMyAnimalsDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordResponseDto;
import com.ashleydev.jokeur_api.exposition.dtos.measure.HealthRecordMeasuresResponseDTO;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordDashboardView;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordMyAnimalsView;

public class HealthRecordMapper {

  public static HealthRecordEntity toEntity(HealthRecordRequestDTO dto, UserEntity user) {
    HealthRecordEntity entity = new HealthRecordEntity();
    entity.setUser(user);
    entity.setPetName(dto.petName());
    entity.setAnimalType(dto.animalType());
    entity.setBreed(dto.breed());
    entity.setSex(dto.sex());
    entity.setBirthDate(dto.birthDate());
    entity.setCurrentWeight(dto.currentWeight());
    entity.setColor(dto.color());
    entity.setIdentificationNumber(dto.identificationNumber());
    entity.setTattoo(dto.tattoo());
    entity.setAllergy(dto.allergy());
    return entity;
  }

  public static HealthRecordDashboardDTO toDashboardDto(HealthRecordDashboardView view) {
    return new HealthRecordDashboardDTO(view.getHealthRecordId(), view.getPetName());
  }

  public static HealthRecordMyAnimalsDTO toMyAnimalsDto(HealthRecordMyAnimalsView view) {
    return new HealthRecordMyAnimalsDTO(
      view.getHealthRecordId(),
      view.getPetName(),
      view.getAnimalType(),
      view.getBreed(),
      view.getSex(),
      view.getCurrentWeight()
    );
  }

  public static HealthRecordResponseDto toDto(HealthRecordEntity entity, HealthRecordMeasuresResponseDTO measures) {
    return new HealthRecordResponseDto(
      entity.getId(),
      entity.getUser().getId(),
      entity.getPetName(),
      entity.getBreed().name(),
      entity.getSex().name(),
      entity.getBirthDate(),
      entity.getCurrentWeight(),
      entity.getColor().name(),
      entity.getIdentificationNumber(),
      entity.getTattoo(),
      entity.getAllergy(),
      entity.getImage(),
      entity.getImageType(),
      entity.getAnimalType().name(),
            entity.getPhotoKey(),
      measures
    );
  }
}
