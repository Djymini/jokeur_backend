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

  @GetMapping("/{healthRecordNumber}/{type}")
  public ResponseEntity<List<MeasureResponseDto>> getAllMeasureOfHealthRecordByType(
    @PathVariable Long healthRecordNumber,
    @PathVariable String type
  ) {
    List<MeasureResponseDto> response = measureService.getByType(type, healthRecordNumber);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<MeasureResponseDto> addMeasure(@RequestBody MeasureRequestDto measure) {
    MeasureResponseDto response = measureService.create(measure);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{healthRecordNumber}/{id}")
  public ResponseEntity<MeasureResponseDto> modifyMeasureById(
    @PathVariable Long healthRecordNumber,
    @PathVariable Long id,
    @RequestBody float value
  ) {
    MeasureResponseDto response = measureService.update(healthRecordNumber, id, value);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{healthRecordNumber}/{id}")
  public ResponseEntity<String> deleteMeasureOfHealthRecordById(@PathVariable Long healthRecordNumber, @PathVariable Long id) {
    String response = measureService.delete(healthRecordNumber, id);
    return ResponseEntity.status(HttpStatus.GONE).body(response);
  }
}
