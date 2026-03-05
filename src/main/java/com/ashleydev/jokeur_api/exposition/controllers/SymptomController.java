package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.SymptomService;
import com.ashleydev.jokeur_api.exposition.dtos.symptom.SymptomRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.symptom.SymptomResponseDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/symptoms")
@AllArgsConstructor
public class SymptomController {

  private final SymptomService symptomService;

  @GetMapping
  public ResponseEntity<Page<SymptomResponseDTO>> getAllSymptoms(@PageableDefault(sort = "id") Pageable pageable) {
    Page<SymptomResponseDTO> response = symptomService.getAllSymptoms(pageable);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<SymptomResponseDTO> getSymptomById(@PathVariable Long id) {
    SymptomResponseDTO response = symptomService.getSymptomById(id);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<SymptomResponseDTO> createSymptom(@RequestBody SymptomResponseDTO dto) {
    SymptomResponseDTO saved = symptomService.createSymptom(dto.name());
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @PutMapping("/{id}")
  public ResponseEntity<SymptomResponseDTO> modifySymptom(@Valid @PathVariable Long id, @RequestBody SymptomRequestDTO dto) {
    SymptomResponseDTO response = symptomService.updateSymptom(id, dto.name());
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteSymptomById(@PathVariable Long id) {
    symptomService.deleteSymptom(id);
    return ResponseEntity.noContent().build();
  }
}
