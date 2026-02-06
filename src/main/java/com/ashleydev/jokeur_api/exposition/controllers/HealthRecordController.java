package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.HealthRecordService;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordDashboardDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordMyAnimalsDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordResponseDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordUpdateDTO;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health-records")
@SuppressWarnings("unused")
public class HealthRecordController {

    private final HealthRecordService healthRecordService;

    @Autowired
    public HealthRecordController(HealthRecordService healthRecordService) {
        this.healthRecordService = healthRecordService;
    }

    @PostMapping
    public ResponseEntity<HealthRecordResponseDTO> createHealthRecord(@Valid @RequestBody HealthRecordRequestDTO dto) {
        HealthRecordResponseDTO saved = healthRecordService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{healthRecordNumber}")
    public HealthRecordResponseDTO getByHealthRecordNumber(@PathVariable Long healthRecordNumber) {
        return healthRecordService.getByHealthRecordNumber(healthRecordNumber);
    }

    @GetMapping("/dashboard")
    public List<HealthRecordDashboardDTO> dashboard(@RequestParam Long ownerId) {
        return healthRecordService.getDashboardByOwner(ownerId);
    }

    @GetMapping("/my-animals")
    public List<HealthRecordMyAnimalsDTO> myAnimals(@RequestParam Long ownerId) {
        return healthRecordService.getMyAnimalsByOwner(ownerId);
    }

    @PatchMapping("/{healthRecordNumber}")
    public HealthRecordResponseDTO updatePartial(
            @PathVariable Long healthRecordNumber,
            @Valid @RequestBody HealthRecordUpdateDTO dto
    ) {
        return healthRecordService.updatePartial(healthRecordNumber, dto);
    }

    @DeleteMapping("/{healthRecordNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByHealthRecordNumber(@PathVariable Long healthRecordNumber) {
        healthRecordService.deleteByHealthRecordNumber(healthRecordNumber);
    }
}
