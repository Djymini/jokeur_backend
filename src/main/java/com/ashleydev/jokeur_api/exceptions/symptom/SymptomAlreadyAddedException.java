package com.ashleydev.jokeur_api.exceptions.symptom;

public class SymptomAlreadyAddedException extends BusinessException {

  public SymptomAlreadyAddedException(Long symptomId, Long healthRecordId) {
    super("Le symptôme " + symptomId + " est déjà associé au carnet de santé " + healthRecordId);
  }
}
