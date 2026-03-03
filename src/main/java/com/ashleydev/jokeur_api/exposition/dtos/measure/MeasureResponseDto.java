package com.ashleydev.jokeur_api.exposition.dtos.measure;

import java.time.LocalDate;

public record MeasureResponseDto(Long id, float value, String measureType, Long healthRecordId, LocalDate creationDate) {}
