package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.domain.rules.HealthRecordRules;
import com.ashleydev.jokeur_api.exceptions.healthRecord.HealthRecordNotFoundException;
import com.ashleydev.jokeur_api.exceptions.owner.OwnerNotFoundException;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.*;
import com.ashleydev.jokeur_api.exposition.mappers.HealthRecordMapper;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.OwnerEntity;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.owner.OwnerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthRecordService {

  @Autowired
  private HealthRecordRepository healthRecordRepository;

  @Autowired
  private OwnerRepository ownerRepository;

  @Autowired
  private HealthRecordRules healthRecordRules;

  public HealthRecordResponseDTO create(HealthRecordRequestDTO dto) {
    healthRecordRules.validateCreate(dto);
    final Long ownerId = dto.ownerId();

    OwnerEntity owner = ownerRepository.findById(ownerId).orElseThrow(() -> new OwnerNotFoundException(ownerId));

    HealthRecordEntity entity = HealthRecordMapper.toEntity(dto, owner);
    HealthRecordEntity saved = healthRecordRepository.save(entity);

    return HealthRecordMapper.toResponseDto(saved);
  }

  public HealthRecordResponseDTO getById(Long healthRecordNumber) {
    return healthRecordRepository.findResponseById(healthRecordNumber).orElseThrow(() -> new HealthRecordNotFoundException(healthRecordNumber));
  }

  public List<HealthRecordDashboardDTO> getDashboardByOwner(Long ownerId) {
    return healthRecordRepository.findByOwner_IdOwner(ownerId).stream().map(HealthRecordMapper::toDashboardDto).toList();
  }

  public List<HealthRecordMyAnimalsDTO> getMyAnimalsByOwner(Long ownerId) {
    return healthRecordRepository.findAllByOwner_IdOwner(ownerId).stream().map(HealthRecordMapper::toMyAnimalsDto).toList();
  }

  public HealthRecordResponseDTO updatePartial(Long healthRecordNumber, HealthRecordUpdateDTO dto) {
    healthRecordRules.validateUpdate(dto);

    HealthRecordEntity entity = healthRecordRepository
      .findById(healthRecordNumber)
      .orElseThrow(() -> new HealthRecordNotFoundException(healthRecordNumber));

    if (dto.getPetName() != null) entity.setPetName(dto.getPetName());
    if (dto.getBreed() != null) entity.setBreed(dto.getBreed());
    if (dto.getSex() != null) entity.setSex(dto.getSex());
    if (dto.getBirthDate() != null) entity.setBirthDate(dto.getBirthDate());
    if (dto.getCurrentWeight() != null) entity.setCurrentWeight(dto.getCurrentWeight());
    if (dto.getColor() != null) entity.setColor(dto.getColor());
    if (dto.getIdentificationNumber() != null) entity.setIdentificationNumber(dto.getIdentificationNumber());
    if (dto.getTattooNumber() != null) entity.setTattooNumber(dto.getTattooNumber());
    if (dto.getAllergy() != null) entity.setAllergy(dto.getAllergy());

    HealthRecordEntity saved = healthRecordRepository.save(entity);
    return HealthRecordMapper.toResponseDto(saved);
  }

  public void delete(Long healthRecordNumber) {
    HealthRecordEntity entity = healthRecordRepository
      .findById(healthRecordNumber)
      .orElseThrow(() -> new HealthRecordNotFoundException(healthRecordNumber));

    healthRecordRepository.delete(entity);
  }
}
