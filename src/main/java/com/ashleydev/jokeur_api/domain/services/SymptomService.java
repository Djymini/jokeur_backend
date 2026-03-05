package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.domain.rules.SymptomRules;
import com.ashleydev.jokeur_api.exposition.dtos.symptom.SymptomResponseDTO;
import com.ashleydev.jokeur_api.mappers.SymptomMapper;
import com.ashleydev.jokeur_api.persistence.entities.SymptomEntity;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SymptomService {

  private final SymptomRepository symptomRepository;
  private final SymptomRules symptomRules;

  public SymptomResponseDTO createSymptom(String name) {
    symptomRules.checkNameNotExists(name);

    SymptomEntity entity = new SymptomEntity();
    entity.setName(name);

    SymptomEntity saved = symptomRepository.save(entity);
    return SymptomMapper.toDto(saved);
  }

  public Page<SymptomResponseDTO> getAllSymptoms(Pageable pageable) {
    return symptomRepository.findAll(pageable).map(SymptomMapper::toDto);
  }

  public SymptomResponseDTO getSymptomById(Long id) {
    symptomRules.checkExists(id);

    SymptomEntity entity = symptomRepository.findById(id).orElseThrow();
    return SymptomMapper.toDto(entity);
  }

  public SymptomResponseDTO updateSymptom(Long id, String name) {
    symptomRules.checkExists(id);
    symptomRules.checkNameNotExists(name);

    SymptomEntity entity = symptomRepository.findById(id).orElseThrow();
    entity.setName(name);
    SymptomEntity saved = symptomRepository.save(entity);
    return SymptomMapper.toDto(saved);
  }

  public void deleteSymptom(Long id) {
    symptomRules.checkExists(id);
    symptomRules.checkNotUsed(id);

    symptomRepository.deleteById(id);
  }
}
