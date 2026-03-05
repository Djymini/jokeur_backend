package com.ashleydev.jokeur_api.exposition.dtos.symptomhealthrecord;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;
public record UpdateSymptomRecordDTO(

        LocalDate endDate,

        @Size(max = 1000, message = "L'observation ne peut pas dépasser 1000 caractères")
        String observation

) {
}
