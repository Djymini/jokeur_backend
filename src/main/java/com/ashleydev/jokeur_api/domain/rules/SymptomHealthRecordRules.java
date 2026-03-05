package com.ashleydev.jokeur_api.domain.rules;

import com.ashleydev.jokeur_api.exceptions.symptom.BusinessException;
import com.ashleydev.jokeur_api.exceptions.symptom.InvalidSymptomDateException;
import com.ashleydev.jokeur_api.exceptions.symptom.SymptomAlreadyAddedException;
import com.ashleydev.jokeur_api.exceptions.symptom.SymptomNotActiveException;
import com.ashleydev.jokeur_api.persistence.entities.SymptomHealthRecordEntity;
import java.time.LocalDate;
import java.util.List;

public class SymptomHealthRecordRules {

  private SymptomHealthRecordRules() {}

  public static void checkSymptomIsActive(SymptomHealthRecordEntity symptomHealthRecord) {
    if (Boolean.FALSE.equals(symptomHealthRecord.getIsActive())) {
      throw new SymptomNotActiveException(symptomHealthRecord.getId());
    }
  }

  public static void checkDateConsistency(LocalDate startdate, LocalDate enddate) {
    if (startdate == null) {
      throw new BusinessException("La date de début est obligatoire");
    }

    if (enddate != null && enddate.isBefore(startdate)) {
      throw new InvalidSymptomDateException();
    }
  }

  public static void checkNotAlreadyAdded(Long symptomId, Long healthRecordId, List<SymptomHealthRecordEntity> existingSymptoms) {
    boolean alreadyExists = existingSymptoms.stream().anyMatch(shr -> shr.getSymptom().getId().equals(symptomId));

    if (alreadyExists) {
      throw new SymptomAlreadyAddedException(symptomId, healthRecordId);
    }
  }
}
