package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.TreatmentService;
import com.ashleydev.jokeur_api.exposition.dtos.treatment.TreatmentDetailRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.treatment.TreatmentRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.treatment.TreatmentResponseDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/treatments")
public class TreatmentController {

  @Autowired
  TreatmentService treatmentService;

  @GetMapping("/{healthRecordId}")
  public ResponseEntity<List<TreatmentResponseDto>> getAllTreatmentOfHealthRecord(@PathVariable Long healthRecordId) {
    List<TreatmentResponseDto> response = treatmentService.getAllByHealthRecordId(healthRecordId);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<TreatmentResponseDto> addTreatment(@RequestBody TreatmentRequestDto treatment) {
    TreatmentResponseDto response = treatmentService.create(treatment);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{healthRecordId}/{id}")
  public ResponseEntity<TreatmentResponseDto> modifyTreatmentById(@RequestBody TreatmentDetailRequestDto treatment) {
    TreatmentResponseDto response = treatmentService.update(treatment);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{healthRecordId}/{id}")
  public ResponseEntity<String> deleteTreatmentOfHealthRecordById(@PathVariable Long healthRecordId, @PathVariable Long id) {
    String response = treatmentService.delete(id, healthRecordId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
  }
}
