package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.MeasureService;
import com.ashleydev.jokeur_api.exposition.dtos.measure.MeasureRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.measure.MeasureResponseDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/measures")
public class MeasureController {

  @Autowired
  MeasureService measureService;

  @GetMapping("/{healthRecordId}/{type}")
  public ResponseEntity<List<MeasureResponseDto>> getAllMeasureOfHealthRecordByType(@PathVariable Long healthRecordId, @PathVariable String type) {
    List<MeasureResponseDto> response = measureService.getByType(type, healthRecordId);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<MeasureResponseDto> addMeasure(@RequestBody MeasureRequestDto measure) {
    MeasureResponseDto response = measureService.create(measure);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{healthRecordId}/{id}")
  public ResponseEntity<MeasureResponseDto> modifyMeasureById(@PathVariable Long healthRecordId, @PathVariable Long id, @RequestBody float value) {
    MeasureResponseDto response = measureService.update(healthRecordId, id, value);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{healthRecordId}/{id}")
  public ResponseEntity<String> deleteMeasureOfHealthRecordById(@PathVariable Long healthRecordId, @PathVariable Long id) {
    String response = measureService.delete(healthRecordId, id);
    return ResponseEntity.status(HttpStatus.GONE).body(response);
  }
}
