package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.HealthRecordExportRequest;
import com.ashleydev.jokeur_api.domain.builders.PdfBuilder;
import com.ashleydev.jokeur_api.domain.builders.XlsxBuilder;
import com.ashleydev.jokeur_api.exceptions.healthRecord.HealthRecordNotFoundException;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HealthRecordExportService {

  private final HealthRecordRepository healthRecordRepository;
  private final PdfBuilder pdfBuilder;
  private final XlsxBuilder xlsxBuilder;

  @Transactional
  public byte[] exportToPdf(Long healthRecordId, HealthRecordExportRequest request) {
    HealthRecordEntity healthRecord = findOrThrow(healthRecordId);
    return pdfBuilder.build(healthRecord, request);
  }

  @Transactional
  public byte[] exportToXlsx(Long healthRecordId, HealthRecordExportRequest request) {
    HealthRecordEntity healthRecord = findOrThrow(healthRecordId);
    return xlsxBuilder.build(healthRecord, request);
  }

  private HealthRecordEntity findOrThrow(Long id) {
    return healthRecordRepository.findById(id).orElseThrow(() -> new HealthRecordNotFoundException(id));
  }
}
