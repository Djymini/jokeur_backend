package com.ashleydev.jokeur_api.exposition.controller;
import com.ashleydev.jokeur_api.domain.services.HealthRecordService;
import com.ashleydev.jokeur_api.exposition.dtos.HealthRecordResponseDto;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/healthrecords")
@AllArgsConstructor
public class HealthRecordController {

    private final HealthRecordService healthRecordService;

    @GetMapping()
    public ResponseEntity<List<HealthRecordResponseDto>> getAllAnimals(@PathParam(value = "idOwner") Long idOwner){
        List<HealthRecordResponseDto> response = healthRecordService.getAllAnimals(idOwner);
        return ResponseEntity.ok(response);
    }
}
