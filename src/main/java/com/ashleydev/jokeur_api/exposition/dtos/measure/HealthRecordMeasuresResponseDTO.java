package com.ashleydev.jokeur_api.exposition.dtos.measure;

import java.util.List;

public record HealthRecordMeasuresResponseDTO(
  List<MeasureResponseDto> temperature,
  List<MeasureResponseDto> weight,
  List<MeasureResponseDto> respiratoryRate,
  List<MeasureResponseDto> bpm
) {}
