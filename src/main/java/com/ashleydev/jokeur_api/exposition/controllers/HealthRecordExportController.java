package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.HealthRecordExportRequest;
import com.ashleydev.jokeur_api.domain.services.HealthRecordExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health-records")
@RequiredArgsConstructor
public class HealthRecordExportController {

  private final HealthRecordExportService exportService;

  @PostMapping(value = "/{healthRecordId}/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> exportPdf(@PathVariable Long healthRecordId, @RequestBody HealthRecordExportRequest request) {
    byte[] pdf = exportService.exportToPdf(healthRecordId, request);
    String filename = "sante_" + healthRecordId + "_" + request.getFrom() + "_" + request.getTo() + ".pdf";

    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
      .contentType(MediaType.APPLICATION_PDF)
      .body(pdf);
  }

  @PostMapping(value = "/{healthRecordId}/export/xlsx", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
  public ResponseEntity<byte[]> exportXlsx(@PathVariable Long healthRecordId, @RequestBody HealthRecordExportRequest request) {
    byte[] xlsx = exportService.exportToXlsx(healthRecordId, request);
    String filename = "sante_" + healthRecordId + "_" + request.getFrom() + "_" + request.getTo() + ".xlsx";

    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
      .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
      .body(xlsx);
  }
}
