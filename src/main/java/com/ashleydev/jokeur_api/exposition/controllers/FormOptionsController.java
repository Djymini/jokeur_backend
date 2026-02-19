package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.enums.pets.AnimalFormOptions;
import com.ashleydev.jokeur_api.exposition.dtos.formOption.FormOptionsResponseDTO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/form-options")
@CrossOrigin(origins = "http://localhost:4200")
public class FormOptionsController {

  @GetMapping
  public FormOptionsResponseDTO getFormOptions() {
    return new FormOptionsResponseDTO(
      AnimalFormOptions.ANIMAL_TYPES,
      AnimalFormOptions.SEXES,
      AnimalFormOptions.COLORS,
      AnimalFormOptions.BREEDS_BY_ANIMAL_TYPE
    );
  }
}
