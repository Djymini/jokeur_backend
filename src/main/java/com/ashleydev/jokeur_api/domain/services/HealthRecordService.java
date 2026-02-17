package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.domain.rules.HealthRecordRules;
import com.ashleydev.jokeur_api.exceptions.healthRecord.HealthRecordNotFoundException;
import com.ashleydev.jokeur_api.exceptions.owner.OwnerNotFoundException;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.*;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordResponseDto;
import com.ashleydev.jokeur_api.exposition.dtos.measure.HealthRecordMeasuresResponseDTO;
import com.ashleydev.jokeur_api.mappers.HealthRecordMapper;
import com.ashleydev.jokeur_api.mappers.MeasureMapper;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.MeasureEntity;
import com.ashleydev.jokeur_api.persistence.entities.OwnerEntity;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.measure.MeasureRepository;
import com.ashleydev.jokeur_api.persistence.repositories.owner.OwnerRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class HealthRecordService {

  private final HealthRecordRepository healthRecordRepository;
  private final MeasureRepository measureRepository;
  private final OwnerRepository ownerRepository;
  private final HealthRecordRules healthRecordRules;

  public HealthRecordResponseDto create(HealthRecordRequestDTO dto) {
    healthRecordRules.validateCreate(dto);

    OwnerEntity owner = ownerRepository.findById(dto.ownerId()).orElseThrow(() -> new OwnerNotFoundException(dto.ownerId()));

    HealthRecordEntity entity = HealthRecordMapper.toEntity(dto, owner);
    HealthRecordEntity saved = healthRecordRepository.save(entity);

    List<MeasureEntity> measureEntities = measureRepository.findByHealthRecordId(saved.getId());
    HealthRecordMeasuresResponseDTO measures = MeasureMapper.toHealthRecordDto(measureEntities);

    return HealthRecordMapper.toDto(saved, measures);
  }

  public HealthRecordResponseDto getByHealthRecordNumber(Long id) {
    HealthRecordEntity entity = healthRecordRepository.findById(id).orElseThrow(() -> new HealthRecordNotFoundException(id));

    List<MeasureEntity> measureEntities = measureRepository.findByHealthRecordId(entity.getId());
    HealthRecordMeasuresResponseDTO measures = MeasureMapper.toHealthRecordDto(measureEntities);

    return HealthRecordMapper.toDto(entity, measures);
  }

  public List<HealthRecordDashboardDTO> getDashboardByOwner(Long ownerId) {
    return healthRecordRepository.findDashboardDtosByOwnerId(ownerId);
  }

  public List<HealthRecordMyAnimalsDTO> getMyAnimalsByOwner(Long ownerId) {
    return healthRecordRepository.findMyAnimalsDtosByOwnerId(ownerId);
  }

  public HealthRecordResponseDto updatePartial(Long healthRecordNumber, HealthRecordUpdateDTO dto) {
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
    if (dto.getId() != null) entity.setIdentificationNumber(dto.getId());
    if (dto.getTattooNumber() != null) entity.setTattooNumber(dto.getTattooNumber());
    if (dto.getAllergy() != null) entity.setAllergy(dto.getAllergy());

    List<MeasureEntity> measureEntities = measureRepository.findByHealthRecordId(entity.getId());
    HealthRecordMeasuresResponseDTO measures = MeasureMapper.toHealthRecordDto(measureEntities);

    return HealthRecordMapper.toDto(healthRecordRepository.save(entity), measures);
  }

  public void deleteByHealthRecordNumber(Long healthRecordNumber) {
    if (!healthRecordRepository.existsById(healthRecordNumber)) {
      throw new HealthRecordNotFoundException(healthRecordNumber);
    }
    healthRecordRepository.deleteById(healthRecordNumber);
  }

  /**
   * permet de récupèrer les information d'un animal pour alimenter la page dashbaoard et page animal
   * @param idOwner
   * @return
   */
  public List<HealthRecordResponseDto> getAllAnimals(Long idOwner) {
    return healthRecordRepository
      .findAllAnimals(idOwner)
      .stream()
      .map(entity -> {
        List<MeasureEntity> measures = measureRepository.findByHealthRecordId(entity.getId());
        HealthRecordMeasuresResponseDTO measuresDto = MeasureMapper.toHealthRecordDto(measures);

        return HealthRecordMapper.toDto(entity, measuresDto);
      })
      .toList();
  }
}
