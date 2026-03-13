package com.ashleydev.jokeur_api.domain.rules;

import com.ashleydev.jokeur_api.exceptions.symptom.BusinessException;
import com.ashleydev.jokeur_api.exceptions.symptom.InvalidSymptomDateException;
import com.ashleydev.jokeur_api.exceptions.symptom.SymptomAlreadyAddedException;
import com.ashleydev.jokeur_api.exceptions.symptom.SymptomNotActiveException;
import com.ashleydev.jokeur_api.exposition.dtos.symptomhealthrecord.AddSymptomToHealthRecordRequestDTO;
import com.ashleydev.jokeur_api.persistence.entities.SymptomHealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomHealthRecordRepository;
import java.time.LocalDate;

public final class SymptomHealthRecordRules {

  private SymptomHealthRecordRules() {}

  public static void checkSymptomIsActive(SymptomHealthRecordEntity symptomHealthRecord) {
    if (!symptomHealthRecord.isActive()) {
      throw new SymptomNotActiveException(symptomHealthRecord.getId());
    }
  }

  public static void checkDateConsistency(LocalDate observationDate, LocalDate endDate) {
    if (observationDate == null) {
      throw new BusinessException("La date d'observation est obligatoire");
    }

    if (endDate != null && endDate.isBefore(observationDate)) {
      throw new InvalidSymptomDateException();
    }
  }

  public static void checkNotAlreadyAdded(SymptomHealthRecordRepository repository, AddSymptomToHealthRecordRequestDTO dto, Long healthRecordId) {
    if (repository.existsByHealthRecordIdAndSymptomIdAndObservationDate(healthRecordId, dto.symptomId(), dto.observationDate())) {
      throw new SymptomAlreadyAddedException(dto.symptomId(), healthRecordId);
    }
  }
}
