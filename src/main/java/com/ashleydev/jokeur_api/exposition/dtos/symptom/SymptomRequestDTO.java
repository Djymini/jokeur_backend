package com.ashleydev.jokeur_api.exposition.dtos.symptom;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SymptomRequestDTO(@NotBlank @Size(max = 100) String name) {}
