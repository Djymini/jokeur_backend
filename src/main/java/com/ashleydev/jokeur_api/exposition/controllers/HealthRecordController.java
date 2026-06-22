package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.HealthRecordService;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.*;
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/health-records")
@AllArgsConstructor
@SuppressWarnings("unused")
public class HealthRecordController {

  private HealthRecordService healthRecordService;

  @GetMapping
  public ResponseEntity<List<HealthRecordResponseDto>> getAllAnimals(@RequestParam(value = "userId", required = false) Long userId) {
    List<HealthRecordResponseDto> response = healthRecordService.getAllAnimals(userId);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<HealthRecordResponseDto> createHealthRecord(@Valid @RequestBody HealthRecordRequestDTO dto) {
    HealthRecordResponseDto saved = healthRecordService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

    @GetMapping("/{id}")
    public HealthRecordResponseDto getByHealthRecordId(
            @PathVariable Long id,
            @AuthenticationPrincipal UserEntity currentUser) {
        return healthRecordService.getByHealthRecordId(id, currentUser);
    }

  @GetMapping("/dashboard")
  public List<HealthRecordDashboardDTO> dashboard(@RequestParam Long userId) {
    return healthRecordService.getDashboardByUser(userId);
  }

  @GetMapping("/my-animals")
  public List<HealthRecordMyAnimalsDTO> myAnimals(@RequestParam Long userId) {
    return healthRecordService.getMyAnimalsByUser(userId);
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

  @PostMapping("/{id}/photo")
  public ResponseEntity<HealthRecordResponseDto> uploadPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
    HealthRecordResponseDto response = healthRecordService.uploadPhoto(id, file);
    return ResponseEntity.ok(response);
  }
}
