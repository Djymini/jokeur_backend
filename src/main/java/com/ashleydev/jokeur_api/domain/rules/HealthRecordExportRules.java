package com.ashleydev.jokeur_api.domain.rules;

import com.ashleydev.jokeur_api.HealthRecordExportRequest;
import com.ashleydev.jokeur_api.domain.enums.MeasureType;
import com.ashleydev.jokeur_api.persistence.entities.MeasureEntity;
import com.ashleydev.jokeur_api.persistence.entities.VaccineEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class HealthRecordExportRules {

  public Map<MeasureType, List<MeasureEntity>> filterMeasuresByTypeAndDateRange(List<MeasureEntity> allMeasures, HealthRecordExportRequest request) {
    return request
      .getMeasureTypes()
      .stream()
      .collect(
        Collectors.toMap(
          measureType -> measureType,
          measureType ->
            allMeasures
              .stream()
              .filter(measure -> measure.getMeasureType() == measureType)
              .filter(measure -> !measure.getCreationDate().isBefore(request.getFrom()) && !measure.getCreationDate().isAfter(request.getTo()))
              .sorted((measureA, measureB) -> measureA.getCreationDate().compareTo(measureB.getCreationDate()))
              .collect(Collectors.toList())
        )
      );
  }

  public List<VaccineEntity> filterVaccinesSortedByDate(List<VaccineEntity> allVaccines) {
    return allVaccines
      .stream()
      .sorted((vaccineA, vaccineB) -> vaccineA.getVaccineDate().compareTo(vaccineB.getVaccineDate()))
      .collect(Collectors.toList());
  }

  public boolean hasMeasureTypes(HealthRecordExportRequest request) {
    return request.getMeasureTypes() != null && !request.getMeasureTypes().isEmpty();
  }
}
