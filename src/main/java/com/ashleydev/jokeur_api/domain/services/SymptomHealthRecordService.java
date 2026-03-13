package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.domain.rules.SymptomHealthRecordRules;
import com.ashleydev.jokeur_api.exceptions.symptom.BusinessException;
import com.ashleydev.jokeur_api.exposition.dtos.symptomhealthrecord.AddSymptomToHealthRecordRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.symptomhealthrecord.SymptomHealthRecordDTO;
import com.ashleydev.jokeur_api.mappers.SymptomHealthRecordMapper;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.SymptomEntity;
import com.ashleydev.jokeur_api.persistence.entities.SymptomHealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomHealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SymptomHealthRecordService {

  private final SymptomHealthRecordRepository symptomHealthRecordRepository;
  private final HealthRecordRepository healthRecordRepository;
  private final SymptomRepository symptomRepository;

  /**
   * vérifie que le health record existe et symptom exist et qu'il n'est pas déjà ajouté
   * ensuite vérifie les date selon le régle dans rules puis il créer l'association
   *
   * @param healthRecordId
   * @param dto
   */
  public void addSymptomToHealthRecord(Long healthRecordId, AddSymptomToHealthRecordRequestDTO dto) {
    HealthRecordEntity healthRecord = healthRecordRepository
      .findById(healthRecordId)
      .orElseThrow(() -> new EntityNotFoundException("HealthRecord introuvable"));

    SymptomEntity symptom = symptomRepository.findById(dto.symptomId()).orElseThrow(() -> new EntityNotFoundException("Symptôme introuvable"));

    SymptomHealthRecordRules.checkNotAlreadyAdded(symptomHealthRecordRepository, dto, healthRecordId);

    SymptomHealthRecordRules.checkDateConsistency(dto.observationDate(), dto.observationDate());

    SymptomHealthRecordEntity entity = new SymptomHealthRecordEntity();
    entity.setHealthRecord(healthRecord);
    entity.setSymptom(symptom);
    entity.setObservationDate(dto.observationDate());
    entity.setEndDate(dto.observationDate());
    entity.setObservation(dto.observation());
    entity.setActive(true);

    symptomHealthRecordRepository.save(entity);
  }

  public List<SymptomHealthRecordDTO> getSymptomsByHealthRecord(Long healthRecordId) {
    return symptomHealthRecordRepository.findByHealthRecordId(healthRecordId).stream().map(SymptomHealthRecordMapper::toDto).toList();
  }

  public void updateSymptomToHealthRecord(Long symptomHealthRecordId, AddSymptomToHealthRecordRequestDTO dto) {
    SymptomHealthRecordEntity symptomHealthRecordEntity = symptomHealthRecordRepository
      .findById(symptomHealthRecordId)
      .orElseThrow(() -> new BusinessException("SymptomHealthRecord introuvable avec id: " + symptomHealthRecordId));

    if (dto.observationDate() != null) {
      symptomHealthRecordEntity.setObservationDate(dto.observationDate());
    }
    if (dto.symptomId() != null) {
      Optional<SymptomEntity> result = symptomRepository.findById(dto.symptomId());

      symptomHealthRecordEntity.setSymptom(result.get());
    }
    if (dto.observation() != null) {
      symptomHealthRecordEntity.setObservation(dto.observation());
    }
    if (dto.active() != null) {
      symptomHealthRecordEntity.setActive(dto.active());
    }
    if (dto.endDate() != null) {
      symptomHealthRecordEntity.setEndDate(dto.endDate());
    }

    symptomHealthRecordRepository.save(symptomHealthRecordEntity);
  }

  public void deactivateSymptom(Long symptomHealthRecordId) {
    SymptomHealthRecordEntity entity = symptomHealthRecordRepository
      .findById(symptomHealthRecordId)
      .orElseThrow(() -> new EntityNotFoundException("SymptomHealthRecord introuvable"));

    SymptomHealthRecordRules.checkSymptomIsActive(entity);

    entity.setActive(false);

    symptomHealthRecordRepository.save(entity);
  }

  public void deleteSymptom(Long symptomHealthRecordId) {
    if (!symptomHealthRecordRepository.existsById(symptomHealthRecordId)) {
      throw new EntityNotFoundException("SymptomHealthRecord introuvable");
    }

    symptomHealthRecordRepository.deleteById(symptomHealthRecordId);
  }
}
