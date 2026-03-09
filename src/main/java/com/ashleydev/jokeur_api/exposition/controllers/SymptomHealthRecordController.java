package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.SymptomHealthRecordService;
import com.ashleydev.jokeur_api.exposition.dtos.symptomhealthrecord.AddSymptomToHealthRecordRequestDTO;
import com.ashleydev.jokeur_api.persistence.entities.SymptomHealthRecordEntity;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/symptom-health-records")
@RequiredArgsConstructor
public class SymptomHealthRecordController {

  private final SymptomHealthRecordService symptomHealthRecordService;

  @PostMapping
  public ResponseEntity<Void> addSymptomToHealthRecord(@Valid @RequestBody AddSymptomToHealthRecordRequestDTO dto) {
    symptomHealthRecordService.addSymptomToHealthRecord(dto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/health-record/{healthRecordId}")
  public ResponseEntity<List<SymptomHealthRecordEntity>> getSymptomsByHealthRecord(@PathVariable Long healthRecordId) {
    return ResponseEntity.ok(symptomHealthRecordService.getSymptomsByHealthRecord(healthRecordId));
  }

  @PatchMapping("/{id}/deactivate")
  public ResponseEntity<Void> deactivateSymptom(@PathVariable Long id) {
    symptomHealthRecordService.deactivateSymptom(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletSymptom(@PathVariable Long id) {
    symptomHealthRecordService.deleteSymptom(id);
    return ResponseEntity.noContent().build();
  }
}
