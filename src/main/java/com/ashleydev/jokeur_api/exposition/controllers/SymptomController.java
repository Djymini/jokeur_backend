package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.SymptomService;
import com.ashleydev.jokeur_api.exposition.dtos.symptom.SymptomResponseDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/symptoms")
@AllArgsConstructor
public class SymptomController {

  private final SymptomService symptomService;

  @GetMapping
  public ResponseEntity<List<SymptomResponseDTO>> getAllSymptoms() {
    return ResponseEntity.ok(symptomService.getAllSymptoms());
  }
}
