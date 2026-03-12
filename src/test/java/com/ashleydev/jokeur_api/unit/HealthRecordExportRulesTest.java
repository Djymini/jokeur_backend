package com.ashleydev.jokeur_api.unit;

import static org.junit.jupiter.api.Assertions.*;

import com.ashleydev.jokeur_api.HealthRecordExportRequest;
import com.ashleydev.jokeur_api.domain.enums.MeasureType;
import com.ashleydev.jokeur_api.domain.rules.HealthRecordExportRules;
import com.ashleydev.jokeur_api.persistence.entities.MeasureEntity;
import com.ashleydev.jokeur_api.persistence.entities.VaccineEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HealthRecordExportRulesTest {

    private HealthRecordExportRules exportRules;

    @BeforeEach
    void setUp() {
        exportRules = new HealthRecordExportRules();
    }

    @Test
    void hasMeasureTypes_shouldReturnFalse_whenMeasureTypesIsNull() {
        HealthRecordExportRequest request = buildRequest(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), null, false);

        assertFalse(exportRules.hasMeasureTypes(request));
    }

    @Test
    void hasMeasureTypes_shouldReturnFalse_whenMeasureTypesIsEmpty() {
        HealthRecordExportRequest request = buildRequest(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), List.of(), false);

        assertFalse(exportRules.hasMeasureTypes(request));
    }

    @Test
    void hasMeasureTypes_shouldReturnTrue_whenMeasureTypesIsNotEmpty() {
        HealthRecordExportRequest request = buildRequest(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), List.of(MeasureType.WEIGHT), false);

        assertTrue(exportRules.hasMeasureTypes(request));
    }

    @Test
    void filterMeasuresByTypeAndDateRange_shouldReturnOnlyMeasuresWithinDateRange() {
        List<MeasureEntity> allMeasures = List.of(
                buildMeasure(MeasureType.WEIGHT, LocalDate.of(2023, 12, 31)), // avant
                buildMeasure(MeasureType.WEIGHT, LocalDate.of(2024, 6, 15)),  // dans la plage
                buildMeasure(MeasureType.WEIGHT, LocalDate.of(2025, 1, 1))    // après
        );

        HealthRecordExportRequest request = buildRequest(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                List.of(MeasureType.WEIGHT),
                false
        );

        Map<MeasureType, List<MeasureEntity>> result = exportRules.filterMeasuresByTypeAndDateRange(allMeasures, request);

        assertEquals(1, result.get(MeasureType.WEIGHT).size());
        assertEquals(LocalDate.of(2024, 6, 15), result.get(MeasureType.WEIGHT).get(0).getCreationDate());
    }

    @Test
    void filterMeasuresByTypeAndDateRange_shouldIncludeMeasuresOnBoundaryDates() {
        List<MeasureEntity> allMeasures = List.of(
                buildMeasure(MeasureType.WEIGHT, LocalDate.of(2024, 1, 1)),   // borne from incluse
                buildMeasure(MeasureType.WEIGHT, LocalDate.of(2024, 12, 31))  // borne to incluse
        );

        HealthRecordExportRequest request = buildRequest(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                List.of(MeasureType.WEIGHT),
                false
        );

        Map<MeasureType, List<MeasureEntity>> result = exportRules.filterMeasuresByTypeAndDateRange(allMeasures, request);

        assertEquals(2, result.get(MeasureType.WEIGHT).size());
    }

    @Test
    void filterMeasuresByTypeAndDateRange_shouldReturnEmptyList_whenNoMeasuresMatchType() {
        List<MeasureEntity> allMeasures = List.of(
                buildMeasure(MeasureType.BPM, LocalDate.of(2024, 6, 1))
        );

        HealthRecordExportRequest request = buildRequest(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                List.of(MeasureType.WEIGHT),
                false
        );

        Map<MeasureType, List<MeasureEntity>> result = exportRules.filterMeasuresByTypeAndDateRange(allMeasures, request);

        assertTrue(result.get(MeasureType.WEIGHT).isEmpty());
    }

    @Test
    void filterMeasuresByTypeAndDateRange_shouldReturnOneEntryPerRequestedType() {
        List<MeasureEntity> allMeasures = List.of(
                buildMeasure(MeasureType.WEIGHT, LocalDate.of(2024, 3, 1)),
                buildMeasure(MeasureType.BPM, LocalDate.of(2024, 4, 1)),
                buildMeasure(MeasureType.TEMPERATURE, LocalDate.of(2024, 5, 1))
        );

        HealthRecordExportRequest request = buildRequest(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                List.of(MeasureType.WEIGHT, MeasureType.BPM),
                false
        );

        Map<MeasureType, List<MeasureEntity>> result = exportRules.filterMeasuresByTypeAndDateRange(allMeasures, request);

        assertEquals(2, result.size());
        assertTrue(result.containsKey(MeasureType.WEIGHT));
        assertTrue(result.containsKey(MeasureType.BPM));
        assertFalse(result.containsKey(MeasureType.TEMPERATURE));
    }

    @Test
    void filterMeasuresByTypeAndDateRange_shouldSortMeasuresByDateAscending() {
        List<MeasureEntity> allMeasures = List.of(
                buildMeasure(MeasureType.WEIGHT, LocalDate.of(2024, 9, 1)),
                buildMeasure(MeasureType.WEIGHT, LocalDate.of(2024, 3, 1)),
                buildMeasure(MeasureType.WEIGHT, LocalDate.of(2024, 6, 1))
        );

        HealthRecordExportRequest request = buildRequest(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                List.of(MeasureType.WEIGHT),
                false
        );

        Map<MeasureType, List<MeasureEntity>> result = exportRules.filterMeasuresByTypeAndDateRange(allMeasures, request);

        List<LocalDate> dates = result.get(MeasureType.WEIGHT).stream()
                .map(MeasureEntity::getCreationDate)
                .toList();

        assertEquals(LocalDate.of(2024, 3, 1), dates.get(0));
        assertEquals(LocalDate.of(2024, 6, 1), dates.get(1));
        assertEquals(LocalDate.of(2024, 9, 1), dates.get(2));
    }

    @Test
    void filterVaccinesSortedByDate_shouldSortVaccinesByDateAscending() {
        List<VaccineEntity> allVaccines = List.of(
                buildVaccine(LocalDate.of(2024, 9, 1)),
                buildVaccine(LocalDate.of(2024, 3, 1)),
                buildVaccine(LocalDate.of(2024, 6, 1))
        );

        List<VaccineEntity> result = exportRules.filterVaccinesSortedByDate(allVaccines);

        assertEquals(LocalDate.of(2024, 3, 1), result.get(0).getVaccineDate());
        assertEquals(LocalDate.of(2024, 6, 1), result.get(1).getVaccineDate());
        assertEquals(LocalDate.of(2024, 9, 1), result.get(2).getVaccineDate());
    }

    @Test
    void filterVaccinesSortedByDate_shouldReturnEmptyList_whenNoVaccines() {
        List<VaccineEntity> result = exportRules.filterVaccinesSortedByDate(List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void filterVaccinesSortedByDate_shouldReturnAllVaccines() {
        List<VaccineEntity> allVaccines = List.of(
                buildVaccine(LocalDate.of(2024, 1, 1)),
                buildVaccine(LocalDate.of(2024, 6, 1))
        );

        List<VaccineEntity> result = exportRules.filterVaccinesSortedByDate(allVaccines);

        assertEquals(2, result.size());
    }

    private MeasureEntity buildMeasure(MeasureType measureType, LocalDate creationDate) {
        MeasureEntity measure = new MeasureEntity();
        measure.setMeasureType(measureType);
        measure.setMeasureValue(42f);
        measure.setCreationDate(creationDate);
        return measure;
    }

    private VaccineEntity buildVaccine(LocalDate vaccineDate) {
        VaccineEntity vaccine = new VaccineEntity();
        vaccine.setVaccineDate(vaccineDate);
        return vaccine;
    }

    private HealthRecordExportRequest buildRequest(
            LocalDate from,
            LocalDate to,
            List<MeasureType> measureTypes,
            boolean includeVaccines
    ) {
        HealthRecordExportRequest request = new HealthRecordExportRequest();
        request.setFrom(from);
        request.setTo(to);
        request.setMeasureTypes(measureTypes);
        request.setIncludeVaccines(includeVaccines);
        return request;
    }
}