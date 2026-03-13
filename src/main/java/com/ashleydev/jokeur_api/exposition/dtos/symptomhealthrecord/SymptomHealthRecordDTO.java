package com.ashleydev.jokeur_api.exposition.dtos.symptomhealthrecord;

import com.ashleydev.jokeur_api.exposition.dtos.symptom.SymptomResponseDTO;
import java.time.LocalDate;

public record SymptomHealthRecordDTO(
  Long id,
  SymptomResponseDTO symptom,
  String symptomName,
  LocalDate observationDate,
  LocalDate endDate,
  String observation,
  Boolean active
) {}
