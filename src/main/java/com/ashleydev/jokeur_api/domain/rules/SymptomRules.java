package com.ashleydev.jokeur_api.domain.rules;

import com.ashleydev.jokeur_api.exceptions.symptom.BusinessException;
import com.ashleydev.jokeur_api.exceptions.symptom.SymptomNotFoundException;
import com.ashleydev.jokeur_api.persistence.entities.SymptomEntity;

public class SymptomRules {

  private SymptomRules() {}

  public static void validateExists(SymptomEntity symptom) {
    if (symptom == null) {
      throw new SymptomNotFoundException("Symptôm non trouvé");
    }
  }

  public static void validateNameNoteExists(boolean namAlreadyExists, String name) {
    if (namAlreadyExists) {
      throw new BusinessException("Un symptôme avec ce nom " + name + " existe déjà.");
    }
  }

  public static void validateNotUsed(boolean isUsed) {
    if (isUsed) {
      throw new BusinessException("Impossible de supprimer le symptôme car il est utilisé dans le carnet de santé.");
    }
  }

  public static void validateName(String name) {
    if (name == null || name.isBlank()) {
      throw new BusinessException("Le nom du symptôme est obligatoire");
    }
  }
}
