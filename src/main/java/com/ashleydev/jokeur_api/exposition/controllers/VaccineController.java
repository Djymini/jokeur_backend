package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.VaccineService;
import com.ashleydev.jokeur_api.exposition.dtos.vaccine.VaccineDetailRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.vaccine.VaccineRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.vaccine.VaccineResponseDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vaccine")
public class VaccineController {

  @Autowired
  VaccineService vaccineService;

  @GetMapping("/{healthRecordId}")
  public ResponseEntity<List<VaccineResponseDto>> getAllVaccineOfHealthRecord(@PathVariable Long healthRecordId) {
    List<VaccineResponseDto> response = vaccineService.getAllByHealthRecordId(healthRecordId);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<VaccineResponseDto> addVaccine(@RequestBody VaccineRequestDto vaccine) {
    VaccineResponseDto response = vaccineService.create(vaccine);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{healthRecordId}/{id}")
  public ResponseEntity<VaccineResponseDto> modifyVaccineById(@RequestBody VaccineDetailRequestDto vaccine) {
    VaccineResponseDto response = vaccineService.update(vaccine);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{healthRecordId}/{id}")
  public ResponseEntity<String> deleteMeasureOfHealthRecordById(@PathVariable Long healthRecordId, @PathVariable Long id) {
    String response = vaccineService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
  }
}
