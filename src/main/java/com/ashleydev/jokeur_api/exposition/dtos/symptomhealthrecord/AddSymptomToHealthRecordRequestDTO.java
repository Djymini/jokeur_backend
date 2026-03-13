package com.ashleydev.jokeur_api.exposition.dtos.symptomhealthrecord;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record AddSymptomToHealthRecordRequestDTO(
  @NotNull(message = "L'ID du symptôme est requis") Long symptomId,

  @NotNull(message = "La date de observation est requise") LocalDate observationDate,

  @Size(max = 1000, message = "L'observation ne peut pas dépasser 1000 caractères") String observation,

  Boolean active,

  LocalDate endDate
) {}
