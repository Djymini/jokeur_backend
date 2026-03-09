package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.HealthRecordExportRequest;
import com.ashleydev.jokeur_api.domain.enums.MeasureType;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.MeasureEntity;
import com.ashleydev.jokeur_api.persistence.entities.VaccineEntity;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
@RequiredArgsConstructor
public class HealthRecordExportService {

  private final HealthRecordRepository healthRecordRepository;
  private final SpringTemplateEngine templateEngine;

  public byte[] exportToPdf(Long healthRecordId, HealthRecordExportRequest request) {
    HealthRecordEntity healthRecord = healthRecordRepository
      .findById(healthRecordId)
      .orElseThrow(() -> new RuntimeException("Health record introuvable"));

    // Mesures filtrées par période et par type sélectionné
    Map<MeasureType, List<MeasureEntity>> measuresByType = null;

    if (request.getMeasureTypes() != null && !request.getMeasureTypes().isEmpty()) {
      measuresByType = request
        .getMeasureTypes()
        .stream()
        .collect(
          Collectors.toMap(
            measureType -> measureType,
            measureType ->
              healthRecord
                .getMeasures()
                .stream()
                .filter(measure -> measure.getMeasureType() == measureType)
                .filter(measure -> !measure.getCreationDate().isBefore(request.getFrom()) && !measure.getCreationDate().isAfter(request.getTo()))
                .sorted((measureA, measureB) -> measureA.getCreationDate().compareTo(measureB.getCreationDate()))
                .collect(Collectors.toList())
          )
        );
    }

    // Vaccins — tous inclus si coché, pas de filtre période
    List<VaccineEntity> vaccines = null;
    if (request.isIncludeVaccines()) {
      vaccines = healthRecord
        .getVaccines()
        .stream()
        .sorted((vaccineA, vaccineB) -> vaccineA.getVaccineDate().compareTo(vaccineB.getVaccineDate()))
        .collect(Collectors.toList());
    }

    // Rendu Thymeleaf
    Context ctx = new Context();
    ctx.setVariable("healthRecord", healthRecord);
    ctx.setVariable("measuresByType", measuresByType);
    ctx.setVariable("vaccines", vaccines);
    ctx.setVariable("from", request.getFrom());
    ctx.setVariable("to", request.getTo());

    String html = templateEngine.process("export/health-record-export", ctx);

    // HTML → PDF
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      ITextRenderer renderer = new ITextRenderer();
      renderer.setDocumentFromString(html);
      renderer.layout();
      renderer.createPDF(baos);
      return baos.toByteArray();
    } catch (Exception exception) {
      throw new RuntimeException("Erreur lors de la génération du PDF", exception);
    }
  }
}
