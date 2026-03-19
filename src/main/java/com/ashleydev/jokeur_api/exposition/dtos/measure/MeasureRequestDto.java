package com.ashleydev.jokeur_api.exposition.dtos.measure;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record MeasureRequestDto(
  @NotNull(message = "value is required") float value,
  @NotNull(message = "type is required") String measureType,
  @NotNull(message = "id is required") Long healthRecordId,
  LocalDate creationDate
) {}
