package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.HealthRecordService;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.*;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health-records")
@AllArgsConstructor
@SuppressWarnings("unused")
public class HealthRecordController {

  private final HealthRecordService healthRecordService;

  @GetMapping
  public ResponseEntity<List<HealthRecordResponseDto>> getAllAnimals(@RequestParam(value = "idOwner", required = false) Long idOwner) {
    List<HealthRecordResponseDto> response = healthRecordService.getAllAnimals(idOwner);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<HealthRecordResponseDto> createHealthRecord(@Valid @RequestBody HealthRecordRequestDTO dto) {
    HealthRecordResponseDto saved = healthRecordService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @GetMapping("/{id}")
  public HealthRecordResponseDto getByHealthRecordId(@PathVariable Long id) {
    return healthRecordService.getByHealthRecordId(id);
  }

  @GetMapping("/dashboard")
  public List<HealthRecordDashboardDTO> dashboard(@RequestParam Long ownerId) {
    return healthRecordService.getDashboardByOwner(ownerId);
  }

  @GetMapping("/my-animals")
  public List<HealthRecordMyAnimalsDTO> myAnimals(@RequestParam Long ownerId) {
    return healthRecordService.getMyAnimalsByOwner(ownerId);
  }

  @PatchMapping("/{id}")
  public HealthRecordResponseDto updatePartial(@PathVariable Long id, @Valid @RequestBody HealthRecordUpdateDTO dto) {
    return healthRecordService.updatePartial(id, dto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteByHealthRecordId(@PathVariable Long id) {
    healthRecordService.deleteByHealthRecordId(id);
  }
}
