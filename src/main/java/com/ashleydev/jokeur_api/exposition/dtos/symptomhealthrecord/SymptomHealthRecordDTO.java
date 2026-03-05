package com.ashleydev.jokeur_api.exposition.dtos.symptomhealthrecord;

import java.time.LocalDate;

public record SymptomHealthRecordDTO(
        Long id,
        Long symptomId,
        String symptomName,
        LocalDate startDate,
        LocalDate endDate,
        String observation,
        Boolean active
) {
}
