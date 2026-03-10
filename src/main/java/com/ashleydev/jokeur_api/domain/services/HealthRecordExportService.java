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

    private static final int COL_DATE = 0;
    private static final int COL_VALUE = 1;
    private static final int COL_UNIT = 2;
    private static final int COL_NAME = 1;
    private static final int COL_VACCINATOR = 2;
    private static final int COL_DESCRIPTION = 3;

    private final HealthRecordRepository healthRecordRepository;
    private final SpringTemplateEngine templateEngine;

    @Transactional
    public byte[] exportToPdf(Long healthRecordId, HealthRecordExportRequest request) {
        HealthRecordEntity healthRecord = healthRecordRepository
                .findById(healthRecordId)
                .orElseThrow(() -> new RuntimeException("Health record introuvable"));

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

        List<VaccineEntity> vaccines = null;
        if (request.isIncludeVaccines()) {
            vaccines = healthRecord
                    .getVaccines()
                    .stream()
                    .sorted((vaccineA, vaccineB) -> vaccineA.getVaccineDate().compareTo(vaccineB.getVaccineDate()))
                    .collect(Collectors.toList());
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
                .orElseThrow(() -> new RuntimeException("Health record introuvable"));

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            CellStyle headerStyle = buildHeaderStyle(workbook);

            if (request.getMeasureTypes() != null && !request.getMeasureTypes().isEmpty()) {
                for (MeasureType measureType : request.getMeasureTypes()) {
                    List<MeasureEntity> measures = healthRecord
                            .getMeasures()
                            .stream()
                            .filter(measure -> measure.getMeasureType() == measureType)
                            .filter(measure -> !measure.getCreationDate().isBefore(request.getFrom()) && !measure.getCreationDate().isAfter(request.getTo()))
                            .sorted((measureA, measureB) -> measureA.getCreationDate().compareTo(measureB.getCreationDate()))
                            .collect(Collectors.toList());

                    Sheet sheet = workbook.createSheet(resolveMeasureLabel(measureType));
                    writeMeasureHeaders(sheet.createRow(0), headerStyle);

                    int rowIndex = 1;
                    for (MeasureEntity measure : measures) {
                        Row row = sheet.createRow(rowIndex++);
                        row.createCell(COL_DATE).setCellValue(measure.getCreationDate().toString());
                        row.createCell(COL_VALUE).setCellValue(measure.getMeasureValue());
                        row.createCell(COL_UNIT).setCellValue(resolveMeasureUnit(measureType));
                    }

                    sheet.autoSizeColumn(COL_DATE);
                    sheet.autoSizeColumn(COL_VALUE);
                    sheet.autoSizeColumn(COL_UNIT);
                }
            }

            if (request.isIncludeVaccines()) {
                Sheet vaccinesSheet = workbook.createSheet("Vaccins");
                writeVaccineHeaders(vaccinesSheet.createRow(0), headerStyle);

                int rowIndex = 1;
                List<VaccineEntity> vaccines = healthRecord
                        .getVaccines()
                        .stream()
                        .sorted((vaccineA, vaccineB) -> vaccineA.getVaccineDate().compareTo(vaccineB.getVaccineDate()))
                        .collect(Collectors.toList());

                for (VaccineEntity vaccine : vaccines) {
                    Row row = vaccinesSheet.createRow(rowIndex++);
                    row.createCell(COL_DATE).setCellValue(vaccine.getVaccineDate().toString());
                    row.createCell(COL_NAME).setCellValue(nullSafe(vaccine.getName()));
                    row.createCell(COL_VACCINATOR).setCellValue(nullSafe(vaccine.getVaccinator()));
                    row.createCell(COL_DESCRIPTION).setCellValue(nullSafe(vaccine.getDescription()));
                }

                vaccinesSheet.autoSizeColumn(COL_DATE);
                vaccinesSheet.autoSizeColumn(COL_NAME);
                vaccinesSheet.autoSizeColumn(COL_VACCINATOR);
                vaccinesSheet.autoSizeColumn(COL_DESCRIPTION);
            }

            workbook.write(baos);
            return baos.toByteArray();

        } catch (Exception exception) {
            throw new RuntimeException("Erreur lors de la génération du fichier Excel", exception);
        }
    }

    private void writeMeasureHeaders(Row row, CellStyle style) {
        applyStyle(row.createCell(COL_DATE), "Date", style);
        applyStyle(row.createCell(COL_VALUE), "Valeur", style);
        applyStyle(row.createCell(COL_UNIT), "Unité", style);
    }

    private void writeVaccineHeaders(Row row, CellStyle style) {
        applyStyle(row.createCell(COL_DATE), "Date", style);
        applyStyle(row.createCell(COL_NAME), "Nom", style);
        applyStyle(row.createCell(COL_VACCINATOR), "Vaccinateur", style);
        applyStyle(row.createCell(COL_DESCRIPTION), "Description", style);
    }

    private void applyStyle(Cell cell, String value, CellStyle style) {
        cell.setCellValue(value);
        cell.setCellStyle(style);
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
}