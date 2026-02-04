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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HealthRecordService {

  private final HealthRecordRepository healthRecordRepository;
  private final OwnerRepository ownerRepository;
  private final HealthRecordRules healthRecordRules;

  public HealthRecordService(HealthRecordRepository healthRecordRepository, OwnerRepository ownerRepository, HealthRecordRules healthRecordRules) {
    this.healthRecordRepository = healthRecordRepository;
    this.ownerRepository = ownerRepository;
    this.healthRecordRules = healthRecordRules;
  }

  public HealthRecordResponseDTO create(HealthRecordRequestDTO dto) {
    healthRecordRules.validateCreate(dto);
    HealthRecordRequestDTO normalizedDto = healthRecordRules.normalize(dto);

    final Long ownerId = normalizedDto.ownerId();

    OwnerEntity owner = ownerRepository.findById(ownerId).orElseThrow(() -> new OwnerNotFoundException(ownerId));

    HealthRecordEntity entity = HealthRecordMapper.toEntity(normalizedDto, owner);

    HealthRecordEntity saved = healthRecordRepository.save(entity);

    return HealthRecordMapper.toResponseDto(saved);
  }

  @Transactional(readOnly = true)
  public HealthRecordResponseDTO getById(Long healthRecordNumber) {
    HealthRecordEntity entity = healthRecordRepository
      .findById(healthRecordNumber)
      .orElseThrow(() -> new HealthRecordNotFoundException(healthRecordNumber));

    return HealthRecordMapper.toResponseDto(entity);
  }

  @Transactional(readOnly = true)
  public List<HealthRecordDashboardDTO> getDashboardByOwner(Long ownerId) {
    return healthRecordRepository.findByOwner_IdOwner(ownerId).stream().map(HealthRecordMapper::toDashboardDto).toList();
  }

  @Transactional(readOnly = true)
  public List<HealthRecordMyAnimalsDTO> getMyAnimalsByOwner(Long ownerId) {
    return healthRecordRepository.findAllByOwner_IdOwner(ownerId).stream().map(HealthRecordMapper::toMyAnimalsDto).toList();
  }

  public HealthRecordResponseDTO updatePartial(Long healthRecordNumber, HealthRecordUpdateDTO dto) {
    healthRecordRules.validateUpdate(dto);
    HealthRecordUpdateDTO normalized = healthRecordRules.normalize(dto);

    HealthRecordEntity entity = healthRecordRepository
      .findById(healthRecordNumber)
      .orElseThrow(() -> new HealthRecordNotFoundException(healthRecordNumber));

    if (normalized.getPetName() != null) entity.setPetName(normalized.getPetName());
    if (normalized.getBreed() != null) entity.setBreed(normalized.getBreed());
    if (normalized.getSex() != null) entity.setSex(normalized.getSex());
    if (normalized.getBirthDate() != null) entity.setBirthDate(normalized.getBirthDate());
    if (normalized.getCurrentWeight() != null) entity.setCurrentWeight(normalized.getCurrentWeight());
    if (normalized.getColor() != null) entity.setColor(normalized.getColor());
    if (normalized.getIdentificationNumber() != null) entity.setIdentificationNumber(normalized.getIdentificationNumber());
    if (normalized.getTattooNumber() != null) entity.setTattooNumber(normalized.getTattooNumber());
    if (normalized.getAllergy() != null) entity.setAllergy(normalized.getAllergy());

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
