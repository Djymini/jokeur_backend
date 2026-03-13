package com.ashleydev.jokeur_api.integration;

import com.ashleydev.jokeur_api.HealthRecordExportRequest;
import com.ashleydev.jokeur_api.domain.enums.MeasureType;
import com.ashleydev.jokeur_api.domain.enums.pets.AnimalType;
import com.ashleydev.jokeur_api.domain.enums.pets.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.pets.PetColor;
import com.ashleydev.jokeur_api.domain.enums.pets.PetSex;
import com.ashleydev.jokeur_api.domain.services.HealthRecordExportService;
import com.ashleydev.jokeur_api.domain.services.HealthRecordService;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.MeasureEntity;
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.measure.MeasureRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = "spring.profiles.active=integration")
@ActiveProfiles("integration")
@Transactional
public class HealthRecordExportServiceIntegrationTest {

    @Autowired
    private HealthRecordExportService exportService;

    @Autowired
    private HealthRecordService healthRecordService;

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Autowired
    private MeasureRepository measureRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private Long healthRecordId;

    @BeforeEach
    void setUp() {
        measureRepository.deleteAll();
        healthRecordRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity user = new UserEntity();
        user.setEmail("export@test.com");
        user.setFirstname("Export");
        user.setName("Test");
        user.setPhoneNumber("0600000001");
        user.setPassword("P@ssword1234");
        Long userId = userRepository.save(user).getId();

        HealthRecordResponseDto healthRecord = healthRecordService.create(new HealthRecordRequestDTO(
                userId,
                "Naya",
                AnimalType.values()[0],
                PetBreed.LABRADOR,
                PetSex.values()[0],
                LocalDate.now().minusYears(2),
                new BigDecimal("4.20"),
                PetColor.BLACK,
                "CHIP-EXPORT-INT-01",
                null,
                null
        ));

        healthRecordId = healthRecord.id();
        HealthRecordEntity healthRecordEntity = healthRecordRepository.findById(healthRecordId).orElseThrow();
        MeasureEntity measureInRange = buildMeasure(MeasureType.WEIGHT, 4.5f, healthRecordEntity);
        MeasureEntity measureOutOfRange = buildMeasure(MeasureType.WEIGHT, 4.8f, healthRecordEntity);
        MeasureEntity bpmMeasure = buildMeasure(MeasureType.BPM, 80f, healthRecordEntity);

        measureRepository.saveAll(List.of(measureInRange, measureOutOfRange, bpmMeasure));
        entityManager.flush();

        entityManager.createQuery(
                        "UPDATE MeasureEntity m SET m.creationDate = :date WHERE m.id = :id")
                .setParameter("date", LocalDate.of(2024, 3, 1))
                .setParameter("id", measureInRange.getId())
                .executeUpdate();

        entityManager.createQuery(
                        "UPDATE MeasureEntity m SET m.creationDate = :date WHERE m.id = :id")
                .setParameter("date", LocalDate.of(2023, 6, 1))
                .setParameter("id", measureOutOfRange.getId())
                .executeUpdate();

        entityManager.createQuery(
                        "UPDATE MeasureEntity m SET m.creationDate = :date WHERE m.id = :id")
                .setParameter("date", LocalDate.of(2024, 4, 1))
                .setParameter("id", bpmMeasure.getId())
                .executeUpdate();

        // Clear le cache de session pour que le service relise depuis la DB
        entityManager.clear();
    }

    @Test
    void exportToXlsx_shouldReturnNonEmptyBytes_whenMeasureTypesProvided() {
        HealthRecordExportRequest request = buildRequest(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                List.of(MeasureType.WEIGHT),
                false
        );

        byte[] result = exportService.exportToXlsx(healthRecordId, request);

        assertThat(result).isNotEmpty();
    }

    @Test
    void exportToXlsx_shouldCreateOneSheetPerSelectedMeasureType() throws Exception {
        HealthRecordExportRequest request = buildRequest(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                List.of(MeasureType.WEIGHT, MeasureType.BPM),
                false
        );

        byte[] result = exportService.exportToXlsx(healthRecordId, request);

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            assertThat(workbook.getNumberOfSheets()).isEqualTo(2);
            assertThat(workbook.getSheet("Poids")).isNotNull();
            assertThat(workbook.getSheet("Fréquence cardiaque")).isNotNull();
        }
    }

    @Test
    void exportToXlsx_shouldOnlyContainMeasuresWithinDateRange() throws Exception {
        // Plage 2024-01-01 → 2024-04-30 : inclut measureInRange (2024-03-01), exclut measureOutOfRange (2023-06-01)
        HealthRecordExportRequest request = buildRequest(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 4, 30),
                List.of(MeasureType.WEIGHT),
                false
        );

        byte[] result = exportService.exportToXlsx(healthRecordId, request);

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            var weightSheet = workbook.getSheet("Poids");
            // ligne 0 = header, ligne 1 = seule mesure dans la plage
            assertThat(weightSheet.getPhysicalNumberOfRows()).isEqualTo(2);
        }
    }

    @Test
    void exportToXlsx_shouldThrow_whenHealthRecordNotFound() {
        HealthRecordExportRequest request = buildRequest(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                List.of(MeasureType.WEIGHT),
                false
        );

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> exportService.exportToXlsx(999999L, request));

        assertThat(exception.getMessage()).isEqualTo("Health record not found with id: 999999");    }

    @Test
    void exportToXlsx_shouldProduceEmptyWorkbook_whenNoMeasureTypesAndNoVaccines() throws Exception {
        HealthRecordExportRequest request = buildRequest(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                List.of(),
                false
        );

        byte[] result = exportService.exportToXlsx(healthRecordId, request);

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            assertThat(workbook.getNumberOfSheets()).isEqualTo(0);
        }
    }

    @Test
    void exportToPdf_shouldReturnNonEmptyBytes() {
        HealthRecordExportRequest request = buildRequest(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                List.of(MeasureType.WEIGHT),
                false
        );

        byte[] result = exportService.exportToPdf(healthRecordId, request);

        assertThat(result).isNotEmpty();
    }

    @Test
    void exportToPdf_shouldThrow_whenHealthRecordNotFound() {
        HealthRecordExportRequest request = buildRequest(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                List.of(),
                false
        );

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> exportService.exportToPdf(999999L, request));

        assertThat(exception.getMessage()).isEqualTo("Health record not found with id: 999999");    }

    private MeasureEntity buildMeasure(MeasureType measureType, float value, HealthRecordEntity healthRecordEntity) {
        MeasureEntity measure = new MeasureEntity();
        measure.setMeasureType(measureType);
        measure.setMeasureValue(value);
        measure.setHealthRecordEntity(healthRecordEntity);
        return measure;
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