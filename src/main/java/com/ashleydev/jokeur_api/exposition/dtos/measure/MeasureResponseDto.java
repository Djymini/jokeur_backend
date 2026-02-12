package com.ashleydev.jokeur_api.exposition.dtos.measure;

import java.time.LocalDate;

public record MeasureResponseDto(Long id, int value, String measureType, Long healthRecordNumber, LocalDate creationDate) {}
