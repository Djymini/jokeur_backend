package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.HealthRecordExportRequest;
import com.ashleydev.jokeur_api.domain.enums.MeasureType;
import com.ashleydev.jokeur_api.domain.rules.HealthRecordExportRules;
import com.ashleydev.jokeur_api.exceptions.healthRecord.HealthRecordNotFoundException;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.MeasureEntity;
import com.ashleydev.jokeur_api.persistence.entities.VaccineEntity;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Service
@RequiredArgsConstructor
public class HealthRecordExportService {

  private final HealthRecordRepository healthRecordRepository;
  private final SpringTemplateEngine templateEngine;
  private final HealthRecordExportRules exportRules;

  @Transactional
  public byte[] exportToPdf(Long healthRecordId, HealthRecordExportRequest request) {
    HealthRecordEntity healthRecord = healthRecordRepository
      .findById(healthRecordId)
      .orElseThrow(() -> new HealthRecordNotFoundException(healthRecordId));

    Map<MeasureType, List<MeasureEntity>> measuresByType = null;
    if (exportRules.hasMeasureTypes(request)) {
      measuresByType = exportRules.filterMeasuresByTypeAndDateRange(healthRecord.getMeasures(), request);
    }

    List<VaccineEntity> vaccines = null;
    if (request.isIncludeVaccines()) {
      vaccines = exportRules.filterVaccinesSortedByDate(healthRecord.getVaccines());
    }

    Context ctx = new Context();
    ctx.setVariable("healthRecord", healthRecord);
    ctx.setVariable("measuresByType", measuresByType);
    ctx.setVariable("vaccines", vaccines);
    ctx.setVariable("from", request.getFrom());
    ctx.setVariable("to", request.getTo());

    String html = templateEngine.process("export/health-record-export", ctx);

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

  @Transactional
  public byte[] exportToXlsx(Long healthRecordId, HealthRecordExportRequest request) {
    HealthRecordEntity healthRecord = healthRecordRepository
      .findById(healthRecordId)
      .orElseThrow(() -> new HealthRecordNotFoundException(healthRecordId));

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      byte[] result = buildXlsx(healthRecord, request);
      baos.write(result);
      return baos.toByteArray();
    } catch (Exception exception) {
      throw new RuntimeException("Erreur lors de la génération du fichier Excel", exception);
    }
  }

  private byte[] buildXlsx(HealthRecordEntity healthRecord, HealthRecordExportRequest request) {
    try (XSSFWorkbook workbook = new XSSFWorkbook()) {
      CellStyle headerStyle = buildHeaderStyle(workbook);

      if (exportRules.hasMeasureTypes(request)) {
        Map<MeasureType, List<MeasureEntity>> measuresByType = exportRules.filterMeasuresByTypeAndDateRange(healthRecord.getMeasures(), request);

        for (MeasureType measureType : request.getMeasureTypes()) {
          List<MeasureEntity> measures = measuresByType.get(measureType);
          Sheet sheet = workbook.createSheet(resolveMeasureLabel(measureType));

          Row headerRow = sheet.createRow(0);
          writeHeaderCell(headerRow, new HeaderCell("Date", 0), headerStyle);
          writeHeaderCell(headerRow, new HeaderCell("Valeur", 1), headerStyle);
          writeHeaderCell(headerRow, new HeaderCell("Unité", 2), headerStyle);

          int rowIndex = 1;
          for (MeasureEntity measure : measures) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(measure.getCreationDate().toString());
            row.createCell(1).setCellValue(measure.getMeasureValue());
            row.createCell(2).setCellValue(resolveMeasureUnit(measureType));
          }

          sheet.autoSizeColumn(0);
          sheet.autoSizeColumn(1);
          sheet.autoSizeColumn(2);
        }
      }

      if (request.isIncludeVaccines()) {
        List<VaccineEntity> vaccines = exportRules.filterVaccinesSortedByDate(healthRecord.getVaccines());

        Sheet vaccinesSheet = workbook.createSheet("Vaccins");

        Row headerRow = vaccinesSheet.createRow(0);
        writeHeaderCell(headerRow, new HeaderCell("Date", 0), headerStyle);
        writeHeaderCell(headerRow, new HeaderCell("Nom", 1), headerStyle);
        writeHeaderCell(headerRow, new HeaderCell("Vaccinateur", 2), headerStyle);
        writeHeaderCell(headerRow, new HeaderCell("Description", 3), headerStyle);

        int rowIndex = 1;
        for (VaccineEntity vaccine : vaccines) {
          Row row = vaccinesSheet.createRow(rowIndex++);
          row.createCell(0).setCellValue(vaccine.getVaccineDate().toString());
          row.createCell(1).setCellValue(nullSafe(vaccine.getName()));
          row.createCell(2).setCellValue(nullSafe(vaccine.getVaccinator()));
          row.createCell(3).setCellValue(nullSafe(vaccine.getDescription()));
        }

        vaccinesSheet.autoSizeColumn(0);
        vaccinesSheet.autoSizeColumn(1);
        vaccinesSheet.autoSizeColumn(2);
        vaccinesSheet.autoSizeColumn(3);
      }

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      workbook.write(out);
      return out.toByteArray();
    } catch (Exception exception) {
      throw new RuntimeException("Erreur lors de la génération du fichier Excel", exception);
    }
  }

  private CellStyle buildHeaderStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setBold(true);
    style.setFont(font);
    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    return style;
  }

  private void writeHeaderCell(Row row, HeaderCell cell, CellStyle style) {
    Cell c = row.createCell(cell.column());
    c.setCellValue(cell.value());
    c.setCellStyle(style);
  }

  private String resolveMeasureLabel(MeasureType measureType) {
    return switch (measureType) {
      case WEIGHT -> "Poids";
      case BPM -> "Fréquence cardiaque";
      case RESPIRATORY_RATE -> "Fréquence respiratoire";
      case TEMPERATURE -> "Température";
    };
  }

  private String resolveMeasureUnit(MeasureType measureType) {
    return switch (measureType) {
      case WEIGHT -> "kg";
      case BPM -> "bpm";
      case RESPIRATORY_RATE -> "rpm";
      case TEMPERATURE -> "°C";
    };
  }

  private String nullSafe(String value) {
    return value != null ? value : "";
  }

  private record HeaderCell(String value, int column) {}
}
