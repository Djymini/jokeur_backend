package com.ashleydev.jokeur_api.domain.rules;

import com.ashleydev.jokeur_api.exceptions.symptom.BusinessException;
import com.ashleydev.jokeur_api.exceptions.symptom.InvalidSymptomDateException;
import com.ashleydev.jokeur_api.exceptions.symptom.SymptomAlreadyAddedException;
import com.ashleydev.jokeur_api.exceptions.symptom.SymptomNotActiveException;
import com.ashleydev.jokeur_api.persistence.entities.SymptomHealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomHealthRecordRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SymptomHealthRecordRules {

  private final SymptomHealthRecordRepository symptomHealthRecordRepository;

  public void checkSymptomIsActive(SymptomHealthRecordEntity symptomHealthRecord) {
    if (Boolean.FALSE.equals(symptomHealthRecord.getIsActive())) {
      throw new SymptomNotActiveException(symptomHealthRecord.getId());
    }
  }

  public void checkDateConsistency(LocalDate observationDate, LocalDate enddate) {
    if (observationDate == null) {
      throw new BusinessException("La date d'observation est obligatoire");
    }

    if (enddate != null && enddate.isBefore(observationDate)) {
      throw new InvalidSymptomDateException();
    }
  }

  public void checkNotAlreadyAdded(Long symptomId, Long healthRecordId) {
    if (symptomHealthRecordRepository.existsByHealthRecordIdAndSymptomId(healthRecordId, symptomId)) {
      throw new SymptomAlreadyAddedException(symptomId, healthRecordId);
    }
  }
}
