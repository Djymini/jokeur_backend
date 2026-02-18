package com.ashleydev.jokeur_api.exposition.dtos.formOption;

import com.ashleydev.jokeur_api.domain.enums.pets.Option;
import java.util.List;
import java.util.Map;

public record FormOptionsResponseDTO(
        List<Option> animalTypes,
        List<Option> sexes,
        List<Option> colors,
        Map<String, List<Option>> breedsByAnimalType
) {}