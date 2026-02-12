package com.ashleydev.jokeur_api.exposition.dtos.measure;

import java.time.LocalDate;

public record MeasureRequestDto(int value, String measureType, Long healthRecordNumber, LocalDate creationDate) {}
