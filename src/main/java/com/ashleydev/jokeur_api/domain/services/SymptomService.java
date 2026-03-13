package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.domain.rules.SymptomRules;
import com.ashleydev.jokeur_api.exposition.dtos.symptom.SymptomResponseDTO;
import com.ashleydev.jokeur_api.mappers.SymptomMapper;
import com.ashleydev.jokeur_api.persistence.entities.SymptomEntity;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomHealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SymptomService {

  private final SymptomRepository symptomRepository;
  private final SymptomHealthRecordRepository symptomHealthRecordRepository;

  public SymptomResponseDTO createSymptom(String name) {
    SymptomRules.checkNameNotExists(name, symptomRepository);

    SymptomEntity entity = new SymptomEntity();
    entity.setName(name);

    SymptomEntity saved = symptomRepository.save(entity);
    return SymptomMapper.toDto(saved);
  }

  public List<SymptomResponseDTO> getAllSymptoms() {
    return symptomRepository.findAll().stream().map(SymptomMapper::toDto).toList();
  }

  public SymptomResponseDTO getSymptomById(Long id) {
    SymptomRules.checkExists(id, symptomRepository);

    SymptomEntity entity = symptomRepository.findById(id).orElseThrow();
    return SymptomMapper.toDto(entity);
  }

  public SymptomResponseDTO updateSymptom(Long id, String name) {
    SymptomRules.checkExists(id, symptomRepository);
    SymptomRules.checkNameNotExists(name, symptomRepository);

    SymptomEntity entity = symptomRepository.findById(id).orElseThrow();
    entity.setName(name);
    SymptomEntity saved = symptomRepository.save(entity);
    return SymptomMapper.toDto(saved);
  }

  public void deleteSymptom(Long id) {
    SymptomRules.checkExists(id, symptomRepository);
    SymptomRules.checkNotUsed(id, symptomHealthRecordRepository);

    symptomRepository.deleteById(id);
  }
}
