package com.ashleydev.jokeur_api.exposition.dtos.symptomhealthrecord;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AddSymptomToHealthRecordDTO(

        @NotNull(message = "L'ID du symptôme est requis")
        Long symptomId,

        @NotNull(message = "La date de début est requise")
        LocalDate startDate,

        LocalDate endDate,

        @Size(max = 1000, message = "L'observation ne peut pas dépasser 1000 caractères")
        String observation
) {
}
