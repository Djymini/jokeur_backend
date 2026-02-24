package com.ashleydev.jokeur_api.integration;

import com.ashleydev.jokeur_api.domain.enums.*;
import com.ashleydev.jokeur_api.domain.enums.pets.AnimalType;
import com.ashleydev.jokeur_api.domain.enums.pets.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.pets.PetColor;
import com.ashleydev.jokeur_api.domain.enums.pets.PetSex;
import com.ashleydev.jokeur_api.domain.services.HealthRecordService;
import com.ashleydev.jokeur_api.domain.services.MeasureService;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordResponseDto;
import com.ashleydev.jokeur_api.exposition.dtos.measure.MeasureRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.measure.MeasureResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.MeasureEntity;
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.measure.MeasureRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.profiles.active=integration")
@ActiveProfiles("integration")
@Transactional
public class MeasureServiceIntegrationTest {
    @Autowired
    private MeasureService measureService;

    @Autowired
    private HealthRecordService healthRecordService;

    @Autowired private MeasureRepository measureRepository;
    @Autowired private HealthRecordRepository healthRecordRepository;
    @Autowired private UserRepository userRepository;

    private Long userId;
    private HealthRecordResponseDto healthRecord;
    private MeasureRequestDto measure1;
    private MeasureRequestDto measure2;
    private MeasureRequestDto measure3;
    private MeasureRequestDto measure4;

    private MeasureResponseDto measureCreated1;
    private MeasureResponseDto measureCreated2;
    private MeasureResponseDto measureCreated3;
    private MeasureResponseDto measureCreated4;

    @BeforeEach
    void setUp() {
        healthRecordRepository.deleteAll();
        userRepository.deleteAll();
        measureRepository.deleteAll();

        UserEntity user = new UserEntity();
        user.setEmail("owner@test.com");
        user.setFirstname("Owner");
        user.setName("Test");
        user.setPhoneNumber("0600000000");
        user.setPassword("P@ssword1234");
        userId = userRepository.save(user).getId();

        HealthRecordRequestDTO healthRecordRequest = new HealthRecordRequestDTO(
                userId,
                "Naya",
                AnimalType.values()[0],
                PetBreed.LABRADOR,
                PetSex.values()[0],
                LocalDate.now().minusYears(2),
                new BigDecimal("4.20"),
                PetColor.BLACK,
                "CHIP12312",
                null,
                null
        );

        healthRecord = healthRecordService.create(healthRecordRequest);

        measure1 = new MeasureRequestDto(5, "weight", healthRecord.id(), LocalDate.now());
        measure2 = new MeasureRequestDto(61, "respiratory_rate", healthRecord.id(), LocalDate.now());
        measure3 = new MeasureRequestDto(63, "respiratory_rate", healthRecord.id(), LocalDate.now());
        measure4 = new MeasureRequestDto(60, "respiratory_rate", healthRecord.id(), LocalDate.now());

        measureCreated1 = measureService.create(measure1);
        measureCreated2 = measureService.create(measure2);
        measureCreated3 = measureService.create(measure3);
        measureCreated4 = measureService.create(measure4);
    }

    @Test
    @Transactional
    void shouldCreateMeasures() {
        List<MeasureEntity> allWeight = measureRepository.findAllByHealthRecordIdAndType(healthRecord.id(), MeasureType.WEIGHT);
        List<MeasureEntity> allRespiratoryRate = measureRepository.findAllByHealthRecordIdAndType(healthRecord.id(), MeasureType.RESPIRATORY_RATE);

        assertThat(allWeight).hasSize(1);
        assertThat(allRespiratoryRate).hasSize(3);

        MeasureEntity persisted = allRespiratoryRate.get(2);
        assertThat(persisted.getId()).isEqualTo(measureCreated4.id());
        assertThat(persisted.getCreationDate()).isEqualTo(LocalDate.now());
        assertThat(persisted.getMeasureType()).isEqualTo(MeasureType.RESPIRATORY_RATE);
        assertThat(persisted.getMeasureValue()).isEqualTo(60);
    }

    @Test
    @Transactional
    void shouldThrowForBadCreation() {
        MeasureRequestDto measureInvalid1 = new MeasureRequestDto(5, "meight", healthRecord.id(), LocalDate.now());
        MeasureRequestDto measureInvalid2 = new MeasureRequestDto(61, "respiratory_rate", 100L, LocalDate.now());

        Exception exception1 = assertThrows(RuntimeException.class,
                () -> measureService.create(measureInvalid1));

        assertTrue(exception1.getMessage().contains("Type is not valid"));

        Exception exception2 = assertThrows(RuntimeException.class,
                () -> measureService.create(measureInvalid2));

        assertTrue(exception2.getMessage().contains("Health record not found with id: 10"));
    }

    @Test
    @Transactional
    void shouldGetMeasures() {
        List<MeasureResponseDto> response = measureService.getByType("respiratory_rate", healthRecord.id());

        List<MeasureEntity> allRespiratoryRate = measureRepository.findAllByHealthRecordIdAndType(healthRecord.id(), MeasureType.RESPIRATORY_RATE);

        assertThat(allRespiratoryRate).hasSize(3);
        assertThat(response).hasSize(allRespiratoryRate.size());

        MeasureResponseDto persisted = response.get(2);
        assertThat(persisted.id()).isEqualTo(measureCreated4.id());
        assertThat(persisted.creationDate()).isEqualTo(LocalDate.now());
        assertThat(persisted.measureType()).isEqualTo("RESPIRATORY_RATE");
        assertThat(persisted.value()).isEqualTo(60);
    }

    @Test
    @Transactional
    void shouldThrowForBadRequestGetMeasure() {
        Exception ex = assertThrows(RuntimeException.class,
                () -> measureService.getByType("respiratory", 1L));

        assertTrue(ex.getMessage().contains("Type is not valid"));
    }

    @Test
    @Transactional
    void shouldGetUpdate() {
        Long measureId = measureCreated4.id();
        float newValue = 74;

        MeasureResponseDto response = measureService.update(healthRecord.id(), measureId, newValue);

        List<MeasureEntity> allRespiratoryRate = measureRepository.findAllByHealthRecordIdAndType(healthRecord.id(), MeasureType.RESPIRATORY_RATE);

        assertThat(response.value()).isEqualTo(newValue);

        MeasureEntity persisted = allRespiratoryRate.get(2);
        assertThat(persisted.getId()).isEqualTo(measureCreated4.id());
        assertThat(persisted.getCreationDate()).isEqualTo(LocalDate.now());
        assertThat(persisted.getMeasureType().toString()).isEqualTo("RESPIRATORY_RATE");
        assertThat(persisted.getMeasureValue()).isEqualTo(74);
    }

    @Test
    @Transactional
    void shouldThrowForBadUpdateRequest() {
        Long measureId = measureCreated4.id();
        float newValue = 74;

        Exception ex = assertThrows(RuntimeException.class,
                () -> measureService.update(999L, measureId, newValue));

        assertTrue(ex.getMessage().contains("The measure : "+measureId+" of Health record number : 999 doesn't exist"));

        Exception ex2 = assertThrows(RuntimeException.class,
                () -> measureService.update(healthRecord.id(), 50L, newValue));

        assertTrue(ex2.getMessage().contains("The measure : 50 of Health record number : "+healthRecord.id()+" doesn't exist"));
    }

    @Test
    @Transactional
    void shouldGetDelete() {
        Long measureId = measureCreated2.id();

        String response = measureService.delete(healthRecord.id(), measureId);

        List<MeasureEntity> allRespiratoryRate = measureRepository.findAllByHealthRecordIdAndType(healthRecord.id(), MeasureType.RESPIRATORY_RATE);

        assertThat(response).isEqualTo("Measure : "+measureId+" is deleted");

        assertThat(allRespiratoryRate).hasSize(2);
        MeasureEntity persisted = allRespiratoryRate.get(0);
        assertThat(persisted.getId()).isEqualTo(measureCreated3.id());
        assertThat(persisted.getCreationDate()).isEqualTo(LocalDate.now());
        assertThat(persisted.getMeasureType().toString()).isEqualTo("RESPIRATORY_RATE");
        assertThat(persisted.getMeasureValue()).isEqualTo(63);
    }
}
