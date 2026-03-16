package com.ashleydev.jokeur_api.domain.builders;

import com.ashleydev.jokeur_api.HealthRecordExportRequest;
import com.ashleydev.jokeur_api.domain.enums.MeasureType;
import com.ashleydev.jokeur_api.domain.rules.HealthRecordExportRules;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.MeasureEntity;
import com.ashleydev.jokeur_api.persistence.entities.VaccineEntity;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class XlsxBuilder {

  @Autowired
  private HealthRecordExportRules exportRules;

  private record ExportContext(XSSFWorkbook workbook, CellStyle headerStyle, HealthRecordEntity healthRecord) {}

  public byte[] build(HealthRecordEntity healthRecord, HealthRecordExportRequest request) {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      XSSFWorkbook workbook = new XSSFWorkbook();
      CellStyle headerStyle = buildHeaderStyle(workbook);
      ExportContext ctx = new ExportContext(workbook, headerStyle, healthRecord);

      if (exportRules.hasMeasureTypes(request)) {
        writeMeasureSheets(ctx, request);
      }

      if (request.isIncludeVaccines()) {
        writeVaccinesSheet(ctx);
      }

      workbook.write(baos);
      return baos.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException("Erreur lors de la génération du fichier Excel", e);
    }
  }

  private void writeMeasureSheets(ExportContext ctx, HealthRecordExportRequest request) {
    Map<MeasureType, List<MeasureEntity>> measuresByType = exportRules.filterMeasuresByTypeAndDateRange(ctx.healthRecord().getMeasures(), request);

    for (MeasureType type : request.getMeasureTypes()) {
      List<MeasureEntity> measures = measuresByType.get(type);
      Sheet sheet = ctx.workbook().createSheet(resolveMeasureLabel(type));

      writeHeaders(sheet.createRow(0), ctx.headerStyle(), "Date", "Valeur", "Unité");

      int rowIndex = 1;
      for (MeasureEntity measure : measures) {
        Row row = sheet.createRow(rowIndex++);
        row.createCell(0).setCellValue(measure.getCreationDate().toString());
        row.createCell(1).setCellValue(measure.getMeasureValue());
        row.createCell(2).setCellValue(resolveMeasureUnit(type));
      }

      autoSize(sheet, 3);
    }
  }

  private void writeVaccinesSheet(ExportContext ctx) {
    List<VaccineEntity> vaccines = exportRules.filterVaccinesSortedByDate(ctx.healthRecord().getVaccines());
    Sheet sheet = ctx.workbook().createSheet("Vaccins");

    writeHeaders(sheet.createRow(0), ctx.headerStyle(), "Date", "Nom", "Vaccinateur", "Description");

    int rowIndex = 1;
    for (VaccineEntity vaccine : vaccines) {
      Row row = sheet.createRow(rowIndex++);
      row.createCell(0).setCellValue(vaccine.getVaccineDate().toString());
      row.createCell(1).setCellValue(nullSafe(vaccine.getName()));
      row.createCell(2).setCellValue(nullSafe(vaccine.getVaccinator()));
      row.createCell(3).setCellValue(nullSafe(vaccine.getDescription()));
    }

    autoSize(sheet, 4);
  }

  private void writeHeaders(Row row, CellStyle style, String... labels) {
    for (int i = 0; i < labels.length; i++) {
      Cell cell = row.createCell(i);
      cell.setCellValue(labels[i]);
      cell.setCellStyle(style);
    }
  }

  private void autoSize(Sheet sheet, int columnCount) {
    for (int i = 0; i < columnCount; i++) {
      sheet.autoSizeColumn(i);
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

  private String resolveMeasureLabel(MeasureType type) {
    return switch (type) {
      case WEIGHT -> "Poids";
      case BPM -> "Fréquence cardiaque";
      case RESPIRATORY_RATE -> "Fréquence respiratoire";
      case TEMPERATURE -> "Température";
    };
  }

  private String resolveMeasureUnit(MeasureType type) {
    return switch (type) {
      case WEIGHT -> "kg";
      case BPM -> "bpm";
      case RESPIRATORY_RATE -> "rpm";
      case TEMPERATURE -> "°C";
    };
  }

  private String nullSafe(String value) {
    return value != null ? value : "";
  }
}
