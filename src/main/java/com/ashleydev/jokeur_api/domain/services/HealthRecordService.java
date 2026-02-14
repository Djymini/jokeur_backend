package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.exposition.dtos.HealthRecordResponseDto;
import com.ashleydev.jokeur_api.mappers.HealthRecordMapper;
import com.ashleydev.jokeur_api.persistence.repositories.HealthRecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HealthRecordService {

  private final HealthRecordRepository healthRecordRepository;
  private final OwnerRepository ownerRepository;
  private final HealthRecordRules healthRecordRules;


  public HealthRecordResponseDTO create(HealthRecordRequestDTO dto) {
    healthRecordRules.validateCreate(dto);

    OwnerEntity owner = ownerRepository.findById(dto.ownerId()).orElseThrow(() -> new OwnerNotFoundException(dto.ownerId()));

    HealthRecordEntity entity = HealthRecordMapper.toEntity(dto, owner);
    HealthRecordEntity saved = healthRecordRepository.save(entity);

    return HealthRecordMapper.toResponseDto(saved);
  }

  public HealthRecordResponseDTO getByHealthRecordNumber(Long healthRecordNumber) {
    HealthRecordEntity entity = healthRecordRepository
      .findByHealthRecordNumber(healthRecordNumber)
      .orElseThrow(() -> new HealthRecordNotFoundException(healthRecordNumber));

    return HealthRecordMapper.toResponseDto(entity);
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

    return HealthRecordMapper.toResponseDto(healthRecordRepository.save(entity));
  }

  public void deleteByHealthRecordNumber(Long healthRecordNumber) {
    if (!healthRecordRepository.existsByHealthRecordNumber(healthRecordNumber)) {
      throw new HealthRecordNotFoundException(healthRecordNumber);
    }
    healthRecordRepository.deleteByHealthRecordNumber(healthRecordNumber);
  }
}
