package com.ashleydev.jokeur_api.domain.enums.pets;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnimalFormOptions {

  public static final List<Option> ANIMAL_TYPES = Arrays.stream(AnimalType.values())
    .map(e -> new Option(e.name(), e.getLabel()))
    .toList();

  public static final List<Option> SEXES = Arrays.stream(PetSex.values())
    .map(e -> new Option(e.name(), e.getLabel()))
    .toList();

  public static final Map<String, List<Option>> BREEDS_BY_ANIMAL_TYPE = Arrays.stream(AnimalType.values()).collect(
    Collectors.toMap(AnimalType::name, type ->
      Arrays.stream(PetBreed.values())
        .filter(b -> b.getAnimalType() == type || b == PetBreed.UNKNOWN)
        .map(b -> new Option(b.name(), b.getLabel()))
        .toList()
    )
  );

  public static final List<Option> COLORS = Arrays.stream(PetColor.values())
    .map(e -> new Option(e.name(), e.getLabel()))
    .toList();

  private AnimalFormOptions() {}
}
