package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.domain.rules.HealthRecordRules;
import com.ashleydev.jokeur_api.exceptions.healthRecord.HealthRecordNotFoundException;
import com.ashleydev.jokeur_api.exceptions.owner.OwnerNotFoundException;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordDashboardDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordMyAnimalsDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordResponseDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordUpdateDTO;
import com.ashleydev.jokeur_api.exposition.dtos.measure.HealthRecordMeasuresResponseDTO;
import com.ashleydev.jokeur_api.exposition.mappers.HealthRecordMapper;
import com.ashleydev.jokeur_api.exposition.mappers.MeasureMapper;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.MeasureEntity;
import com.ashleydev.jokeur_api.persistence.entities.OwnerEntity;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.measure.MeasureRepository;
import com.ashleydev.jokeur_api.persistence.repositories.owner.OwnerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthRecordService {

  private final HealthRecordRepository healthRecordRepository;
  private final OwnerRepository ownerRepository;
  private final HealthRecordRules healthRecordRules;

  @Autowired
  private MeasureRepository measureRepository;

  @Autowired
  public HealthRecordService(HealthRecordRepository healthRecordRepository, OwnerRepository ownerRepository, HealthRecordRules healthRecordRules) {
    this.healthRecordRepository = healthRecordRepository;
    this.ownerRepository = ownerRepository;
    this.healthRecordRules = healthRecordRules;
  }

  public HealthRecordResponseDTO create(HealthRecordRequestDTO dto) {
    healthRecordRules.validateCreate(dto);

    OwnerEntity owner = ownerRepository.findById(dto.ownerId()).orElseThrow(() -> new OwnerNotFoundException(dto.ownerId()));

    HealthRecordEntity entity = HealthRecordMapper.toEntity(dto, owner);
    HealthRecordEntity saved = healthRecordRepository.save(entity);

    List<MeasureEntity> measureEntities = measureRepository.findByHealthRecordNumber(saved.getHealthRecordNumber());
    HealthRecordMeasuresResponseDTO measures = MeasureMapper.toHealthRecordDto(measureEntities);

    return HealthRecordMapper.toResponseDto(saved, measures);
  }

  public HealthRecordResponseDTO getByHealthRecordNumber(Long healthRecordNumber) {
    HealthRecordEntity entity = healthRecordRepository
      .findByHealthRecordNumber(healthRecordNumber)
      .orElseThrow(() -> new HealthRecordNotFoundException(healthRecordNumber));

    List<MeasureEntity> measureEntities = measureRepository.findByHealthRecordNumber(entity.getHealthRecordNumber());
    HealthRecordMeasuresResponseDTO measures = MeasureMapper.toHealthRecordDto(measureEntities);

    return HealthRecordMapper.toResponseDto(entity, measures);
  }

  public List<HealthRecordDashboardDTO> getDashboardByOwner(Long ownerId) {
    return healthRecordRepository.findDashboardDtosByOwnerId(ownerId);
  }

  public List<HealthRecordMyAnimalsDTO> getMyAnimalsByOwner(Long ownerId) {
    return healthRecordRepository.findMyAnimalsDtosByOwnerId(ownerId);
  }

  public HealthRecordResponseDTO updatePartial(Long healthRecordNumber, HealthRecordUpdateDTO dto) {
    healthRecordRules.validateUpdate(dto);

    HealthRecordEntity entity = healthRecordRepository
      .findByHealthRecordNumber(healthRecordNumber)
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

    List<MeasureEntity> measureEntities = measureRepository.findByHealthRecordNumber(entity.getHealthRecordNumber());
    HealthRecordMeasuresResponseDTO measures = MeasureMapper.toHealthRecordDto(measureEntities);

    return HealthRecordMapper.toResponseDto(healthRecordRepository.save(entity), measures);
  }

  public void deleteByHealthRecordNumber(Long healthRecordNumber) {
    if (!healthRecordRepository.existsByHealthRecordNumber(healthRecordNumber)) {
      throw new HealthRecordNotFoundException(healthRecordNumber);
    }
    healthRecordRepository.deleteByHealthRecordNumber(healthRecordNumber);
  }
}
