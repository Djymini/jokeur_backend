package com.ashleydev.jokeur_api.exposition.mappers;

import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordDashboardDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordMyAnimalsDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordResponseDTO;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.OwnerEntity;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordDashboardView;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordMyAnimalsView;

public class HealthRecordMapper {

  public static HealthRecordEntity toEntity(HealthRecordRequestDTO dto, OwnerEntity owner) {
    HealthRecordEntity entity = new HealthRecordEntity();
    entity.setOwner(owner);
    entity.setPetName(dto.petName());
    entity.setAnimalType(dto.animalType());
    entity.setBreed(dto.breed());
    entity.setSex(dto.sex());
    entity.setBirthDate(dto.birthDate());
    entity.setCurrentWeight(dto.currentWeight());
    entity.setColor(dto.color());
    entity.setIdentificationNumber(dto.identificationNumber());
    entity.setTattooNumber(dto.tattooNumber());
    entity.setAllergy(dto.allergy());
    return entity;
  }

  public static HealthRecordResponseDTO toResponseDto(HealthRecordEntity entity) {
    return new HealthRecordResponseDTO(
      entity.getHealthRecordNumber(),
      entity.getOwner().getIdOwner(),
      entity.getPetName(),
      entity.getAnimalType(),
      entity.getBreed(),
      entity.getSex(),
      entity.getBirthDate(),
      entity.getCurrentWeight(),
      entity.getColor(),
      entity.getIdentificationNumber(),
      entity.getTattooNumber(),
      entity.getAllergy()
    );
  }

  public static HealthRecordDashboardDTO toDashboardDto(HealthRecordDashboardView view) {
    return new HealthRecordDashboardDTO(view.getHealthRecordNumber(), view.getPetName());
  }

  public static HealthRecordMyAnimalsDTO toMyAnimalsDto(HealthRecordMyAnimalsView view) {
    return new HealthRecordMyAnimalsDTO(
      view.getHealthRecordNumber(),
      view.getPetName(),
      view.getAnimalType(),
      view.getBreed(),
      view.getSex(),
      view.getCurrentWeight()
    );
  }
}
