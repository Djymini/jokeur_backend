package com.ashleydev.jokeur_api.domain.rules;

import com.ashleydev.jokeur_api.exceptions.symptom.BusinessException;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomHealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomRepository;

public class SymptomRules {

  private SymptomRules() {}

  public static void checkExists(Long id, SymptomRepository symptomRepository) {
    if (!symptomRepository.existsById(id)) {
      throw new BusinessException("Symptom non trouvé avec id: " + id);
    }
  }

  public static void checkNameNotExists(String name, SymptomRepository symptomRepository) {
    if (symptomRepository.existsByNameIgnoreCase(name)) {
      throw new BusinessException("Un symptôme avec ce nom " + name + " existe déjà.");
    }
  }

  public static void checkNotUsed(Long symptomId, SymptomHealthRecordRepository symptomHealthRecordRepository) {
    if (symptomHealthRecordRepository.existsBySymptomId(symptomId)) {
      throw new BusinessException("Impossible de supprimer le symptôme car il est utilisé dans le carnet de santé.");
    }
  }
}
