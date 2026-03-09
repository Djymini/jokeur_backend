package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.domain.rules.SymptomHealthRecordRules;
import com.ashleydev.jokeur_api.exposition.dtos.symptomhealthrecord.AddSymptomToHealthRecordRequestDTO;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.SymptomEntity;
import com.ashleydev.jokeur_api.persistence.entities.SymptomHealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomHealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
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
  private final SymptomHealthRecordRules rules;

  /**
   * vérifie que le health record existe et symptom exist et qu'il n'est pas déjà ajouté
   * ensuite vérifie les date selon le régle dans rules puis il créer l'association
   * @param dto
   */
  public void addSymptomToHealthRecord(AddSymptomToHealthRecordRequestDTO dto) {
    HealthRecordEntity healthRecord = healthRecordRepository
      .findById(dto.healthRecordId())
      .orElseThrow(() -> new EntityNotFoundException("HealthRecord introuvable"));

    SymptomEntity symptom = symptomRepository.findById(dto.symptomId()).orElseThrow(() -> new EntityNotFoundException("Symptôme introuvable"));

    rules.checkNotAlreadyAdded(dto.symptomId(), dto.healthRecordId());

    rules.checkDateConsistency(dto.observationDate(), dto.observationDate());

    SymptomHealthRecordEntity entity = new SymptomHealthRecordEntity();
    entity.setHealthRecord(healthRecord);
    entity.setSymptom(symptom);
    entity.setObservationDate(dto.observationDate());
    entity.setEndDate(dto.observationDate());
    entity.setObservation(dto.observation());
    entity.setIsActive(true);

    symptomHealthRecordRepository.save(entity);
  }

  public List<SymptomHealthRecordEntity> getSymptomsByHealthRecord(Long healthRecordId) {
    return symptomHealthRecordRepository.findByHealthRecordId(healthRecordId);
  }

  public void deactivateSymptom(Long symptomHealthRecordId) {
    SymptomHealthRecordEntity entity = symptomHealthRecordRepository
      .findById(symptomHealthRecordId)
      .orElseThrow(() -> new EntityNotFoundException("SymptomHealthRecord introuvable"));

    rules.checkSymptomIsActive(entity);

    entity.setIsActive(false);

    symptomHealthRecordRepository.save(entity);
  }

  public void deleteSymptom(Long symptomHealthRecordId) {
    if (!symptomHealthRecordRepository.existsById(symptomHealthRecordId)) {
      throw new EntityNotFoundException("SymptomHealthRecord introuvable");
    }

    symptomHealthRecordRepository.deleteById(symptomHealthRecordId);
  }
}
