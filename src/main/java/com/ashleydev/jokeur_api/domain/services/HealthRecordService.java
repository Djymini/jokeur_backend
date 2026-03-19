package com.ashleydev.jokeur_api.domain.services;

import static org.aspectj.runtime.internal.Conversions.floatValue;

import com.ashleydev.jokeur_api.domain.rules.HealthRecordRules;
import com.ashleydev.jokeur_api.exceptions.healthRecord.HealthRecordNotFoundException;
import com.ashleydev.jokeur_api.exceptions.owner.OwnerNotFoundException;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.*;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordResponseDto;
import com.ashleydev.jokeur_api.exposition.dtos.measure.HealthRecordMeasuresResponseDTO;
import com.ashleydev.jokeur_api.exposition.dtos.measure.MeasureRequestDto;
import com.ashleydev.jokeur_api.mappers.HealthRecordMapper;
import com.ashleydev.jokeur_api.mappers.MeasureMapper;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.MeasureEntity;
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.measure.MeasureRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Transactional
public class HealthRecordService {

  private final HealthRecordRepository healthRecordRepository;
  private final MeasureRepository measureRepository;
  private final UserRepository userRepository;
  private final HealthRecordRules healthRecordRules;
  private final StorageService storageService;
  private final MeasureService measureService;

  public HealthRecordResponseDto create(HealthRecordRequestDTO dto) {
    healthRecordRules.validateCreate(dto);

    UserEntity user = userRepository.findById(dto.userId()).orElseThrow(() -> new OwnerNotFoundException(dto.userId()));

    HealthRecordEntity entity = HealthRecordMapper.toEntity(dto, user);
    HealthRecordEntity saved = healthRecordRepository.save(entity);

    saveCurrentWeight(saved);

    List<MeasureEntity> measureEntities = measureRepository.findByHealthRecordId(saved.getId());
    HealthRecordMeasuresResponseDTO measures = MeasureMapper.toHealthRecordDto(measureEntities);

    return HealthRecordMapper.toDto(saved, measures);
  }

  public HealthRecordResponseDto getByHealthRecordId(Long id) {
    HealthRecordEntity entity = healthRecordRepository.findById(id).orElseThrow(() -> new HealthRecordNotFoundException(id));

    List<MeasureEntity> measureEntities = measureRepository.findByHealthRecordId(entity.getId());
    HealthRecordMeasuresResponseDTO measures = MeasureMapper.toHealthRecordDto(measureEntities);

    return HealthRecordMapper.toDto(entity, measures);
  }

  public List<HealthRecordDashboardDTO> getDashboardByUser(Long userId) {
    return healthRecordRepository.findDashboardDtosByUserId(userId);
  }

  public List<HealthRecordMyAnimalsDTO> getMyAnimalsByUser(Long userId) {
    return healthRecordRepository.findMyAnimalsDtosByUserId(userId);
  }

  public HealthRecordResponseDto updatePartial(Long id, HealthRecordUpdateDTO dto) {
    healthRecordRules.validateUpdate(dto);

    HealthRecordEntity entity = healthRecordRepository.findById(id).orElseThrow(() -> new HealthRecordNotFoundException(id));

    if (dto.getPetName() != null) entity.setPetName(dto.getPetName());
    if (dto.getBreed() != null) entity.setBreed(dto.getBreed());
    if (dto.getSex() != null) entity.setSex(dto.getSex());
    if (dto.getBirthDate() != null) entity.setBirthDate(dto.getBirthDate());
    if (dto.getCurrentWeight() != null) entity.setCurrentWeight(dto.getCurrentWeight());
    if (dto.getColor() != null) entity.setColor(dto.getColor());
    if (dto.getIdentificationNumber() != null) entity.setIdentificationNumber(dto.getIdentificationNumber());
    if (dto.getTattoo() != null) entity.setTattoo(dto.getTattoo());
    if (dto.getAllergy() != null) entity.setAllergy(dto.getAllergy());

    List<MeasureEntity> measureEntities = measureRepository.findByHealthRecordId(entity.getId());
    HealthRecordMeasuresResponseDTO measures = MeasureMapper.toHealthRecordDto(measureEntities);

    return HealthRecordMapper.toDto(healthRecordRepository.save(entity), measures);
  }

  public void deleteByHealthRecordId(Long id) {
    if (!healthRecordRepository.existsById(id)) {
      throw new HealthRecordNotFoundException(id);
    }
    healthRecordRepository.deleteById(id);
  }

  public List<HealthRecordResponseDto> getAllAnimals(Long userId) {
    return healthRecordRepository
      .findAllAnimals(userId)
      .stream()
      .map(entity -> {
        List<MeasureEntity> measures = measureRepository.findByHealthRecordId(entity.getId());
        HealthRecordMeasuresResponseDTO measuresDto = MeasureMapper.toHealthRecordDto(measures);

        return HealthRecordMapper.toDto(entity, measuresDto);
      })
      .toList();
  }

  public HealthRecordResponseDto uploadPhoto(Long id, MultipartFile file) {
    HealthRecordEntity entity = healthRecordRepository.findById(id).orElseThrow(() -> new HealthRecordNotFoundException(id));

    if (entity.getPhotoKey() != null) {
      storageService.delete(entity.getPhotoKey());
    }

    String photoKey = storageService.store(file, id);
    entity.setPhotoKey(photoKey);

    HealthRecordEntity saved = healthRecordRepository.save(entity);
    List<MeasureEntity> measures = measureRepository.findByHealthRecordId(saved.getId());
    HealthRecordMeasuresResponseDTO measuresDto = MeasureMapper.toHealthRecordDto(measures);

    return HealthRecordMapper.toDto(saved, measuresDto);
  }

  private void saveCurrentWeight(HealthRecordEntity entity) {
    MeasureRequestDto currentWeightForSave = new MeasureRequestDto(floatValue(entity.getCurrentWeight()), "WEIGHT", entity.getId(), LocalDate.now());

    measureService.create(currentWeightForSave);
  }
}
