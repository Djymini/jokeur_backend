package com.ashleydev.jokeur_api.integration;

import com.ashleydev.jokeur_api.domain.enums.ReminderStatus;
import com.ashleydev.jokeur_api.domain.enums.TreatmentFrequencyType;
import com.ashleydev.jokeur_api.domain.enums.pets.AnimalType;
import com.ashleydev.jokeur_api.domain.enums.pets.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.pets.PetColor;
import com.ashleydev.jokeur_api.domain.enums.pets.PetSex;
import com.ashleydev.jokeur_api.domain.services.HealthRecordService;
import com.ashleydev.jokeur_api.domain.services.TreatmentService;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordResponseDto;
import com.ashleydev.jokeur_api.exposition.dtos.reminder.ReminderVaccineRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.treatment.TreatmentDetailRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.treatment.TreatmentRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.treatment.TreatmentResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.TreatmentEntity;
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import com.ashleydev.jokeur_api.persistence.repositories.TreatmentRepository;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.profiles.active=integration")
@ActiveProfiles("integration")
@Transactional
public class TreatmentServiceIntegrationTest {
    @Autowired
    private TreatmentService treatmentService;

    @Autowired
    private HealthRecordService healthRecordService;

    @Autowired private TreatmentRepository treatmentRepository;
    @Autowired private HealthRecordRepository healthRecordRepository;
    @Autowired private UserRepository userRepository;

    private Long userId;
    private HealthRecordResponseDto healthRecord;

    private TreatmentRequestDto treatment1;
    private TreatmentRequestDto treatment2;
    private TreatmentRequestDto treatment3;
    private TreatmentRequestDto treatment4;

    private TreatmentResponseDto treatmentCreated1;
    private TreatmentResponseDto treatmentCreated2;
    private TreatmentResponseDto treatmentCreated3;
    private TreatmentResponseDto treatmentCreated4;

    @BeforeEach
    void setUp() {
        healthRecordRepository.deleteAll();
        userRepository.deleteAll();
        treatmentRepository.deleteAll();

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

        treatment1 = new TreatmentRequestDto("Betadine", "Desinfectant coupure", TreatmentFrequencyType.DAILY, LocalDate.now(), null, LocalDateTime.of(2026, 12, 12,9,30), healthRecord.id());
        treatment2 = new TreatmentRequestDto("Doliprane", "Lutte contre les mots de tete", TreatmentFrequencyType.DAILY, LocalDate.now(), null, LocalDateTime.of(2026, 12, 12,9,30), healthRecord.id());
        treatment3 = new TreatmentRequestDto("Prozate", "Lutte contre les mots de ventre", TreatmentFrequencyType.DAILY, LocalDate.now(), null, LocalDateTime.of(2026, 12, 12,9,30), healthRecord.id());
        treatment4 = new TreatmentRequestDto("Spacefon", "Lutte contre les mots de ventre", TreatmentFrequencyType.DAILY, LocalDate.now(), null, LocalDateTime.of(2026, 12, 12,9,30), healthRecord.id());

        treatmentCreated1 = treatmentService.create(treatment1);
        treatmentCreated2 = treatmentService.create(treatment2);
        treatmentCreated3 = treatmentService.create(treatment3);
        treatmentCreated4 = treatmentService.create(treatment4);
    }

    @Test
    @Transactional
    void shouldCreateTreatments() {
        List<TreatmentEntity> allTreatment = treatmentRepository.findAllByHealthRecordI(healthRecord.id());

        assertThat(allTreatment).hasSize(4);

        TreatmentEntity persisted = allTreatment.get(2);
        assertThat(persisted.getId()).isEqualTo(treatmentCreated3.id());
        assertThat(persisted.getReminderEntity().getReminderDate()).isEqualTo(LocalDateTime.of(2026, 12, 12,9,30));
        assertThat(persisted.getFrequency()).isEqualTo(treatment3.frequency());
        assertThat(persisted.getName()).isEqualTo(treatment3.name());

        assertThat(persisted.getReminderEntity().getDescription()).isEqualTo("Rappel pour le traitement : " +treatment3.name() + " de " + healthRecord.petName());
        assertThat(persisted.getReminderEntity().getUser().getId()).isEqualTo(healthRecord.userId());
    }

    @Test
    @Transactional
    void shouldThrowForBadCreation() {
        TreatmentRequestDto treatmentInvalid = new TreatmentRequestDto("Betadine", "Desinfectant coupure", TreatmentFrequencyType.DAILY, LocalDate.now(), null, LocalDateTime.of(2026, 12, 12,9,30), 999L);

        Exception exception1 = assertThrows(RuntimeException.class,
                () -> treatmentService.create(treatmentInvalid));

        assertTrue(exception1.getMessage().contains("Health record not found with id: 999"));
    }

    @Test
    @Transactional
    void shouldGetTreatmentsByHealthRecord() {
        List<TreatmentEntity> allTreatments = treatmentRepository.findAllByHealthRecordI(healthRecord.id());
        List<TreatmentResponseDto> response = treatmentService.getAllByHealthRecordId(healthRecord.id());

        assertThat(allTreatments).hasSize(response.size());

        TreatmentEntity persisted = allTreatments.get(2);
        TreatmentResponseDto test = response.get(2);
        assertThat(persisted.getId()).isEqualTo(test.id());
        assertThat(persisted.getName()).isEqualTo(test.name());
    }

    @Test
    @Transactional
    void shouldThrowForBadRequestGetTreatmentByHealthRecord() {
        Long invalidhealthRecordId = 999L;
        Exception exception1 = assertThrows(RuntimeException.class,
                () -> treatmentService.getAllByHealthRecordId(invalidhealthRecordId));

        assertTrue(exception1.getMessage().contains("Health record not found with id: 999"));
    }

    @Test
    @Transactional
    void shouldGetTreatmentsById() {
        TreatmentEntity treatmentPersisted = treatmentRepository.findById(treatmentCreated1.id()).get();
        TreatmentResponseDto response = treatmentService.getById(treatmentCreated1.id());

        assertThat(treatmentPersisted.getId()).isEqualTo(response.id());
        assertThat(treatmentPersisted.getName()).isEqualTo(response.name());
    }

    @Test
    @Transactional
    void shouldThrowForBadRequestGetMeasure() {
        Long invalidId = 999L;
        Exception exception1 = assertThrows(RuntimeException.class,
                () -> treatmentService.getById(invalidId));

        assertTrue(exception1.getMessage().contains("Treatment not found with id: " + invalidId));
    }

    @Test
    @Transactional
    void shouldGetUpdate() {
        TreatmentEntity persisted = treatmentRepository.findById(treatmentCreated1.id()).get();
        ReminderVaccineRequestDto newReminder = new ReminderVaccineRequestDto(treatmentCreated1.reminder().id(), "new description for reminder", LocalDateTime.of(2026, 6, 6,9,30), ReminderStatus.PENDING);
        TreatmentDetailRequestDto request = new TreatmentDetailRequestDto(treatmentCreated1.id(), "Tester", "new desciption", TreatmentFrequencyType.MONTHLY, LocalDate.now(), LocalDate.of(2007, 12, 5), healthRecord.id(), newReminder);

        treatmentService.update(request);
        TreatmentResponseDto test = treatmentService.getById(treatmentCreated1.id());

        assertThat(test.id()).isEqualTo(persisted.getId());
        assertThat(test.description()).isEqualTo(request.description());
        assertThat(test.reminder().reminderDate()).isEqualTo(newReminder.reminderDate());
        assertThat(test.name()).isEqualTo(request.name());
    }

    @Test
    @Transactional
    void shouldThrowForBadUpdateRequest() {
        TreatmentEntity persisted = treatmentRepository.findById(treatmentCreated1.id()).get();
        ReminderVaccineRequestDto newReminder = new ReminderVaccineRequestDto(1L, "new description for reminder", LocalDateTime.of(2026, 6, 6,9,30), ReminderStatus.PENDING);
        TreatmentDetailRequestDto invalidRequest1 = new TreatmentDetailRequestDto(999L, "Tester", "new desciption", TreatmentFrequencyType.MONTHLY, LocalDate.now(), LocalDate.of(2007, 12, 5), healthRecord.id(), newReminder);
        TreatmentDetailRequestDto invalidRequest2 = new TreatmentDetailRequestDto(1L, "Tester", "new desciption", TreatmentFrequencyType.MONTHLY, LocalDate.now(), LocalDate.of(2007, 12, 5), 999L, newReminder);

        Exception ex = assertThrows(RuntimeException.class,
                () -> treatmentService.update(invalidRequest1));

        assertTrue(ex.getMessage().contains("Treatment not found with id: 999"));

        Exception ex2 = assertThrows(RuntimeException.class,
                () -> treatmentService.update(invalidRequest2));

        assertTrue(ex2.getMessage().contains("Health record not found with id: 999"));
    }

    @Test
    @Transactional
    void shouldGetDelete() {
        List<TreatmentEntity> valueToTest = treatmentRepository.findAll();
        String response = treatmentService.delete(treatmentCreated2.id(), healthRecord.id());
        List<TreatmentEntity> persisted = treatmentRepository.findAll();

        assertThat(response).isEqualTo("Treatment : " + treatmentCreated2.id() + " is deleted");

        assertThat(persisted.size()).isEqualTo(valueToTest.size()-1);
        assertThat(persisted.get(1).getName()).isEqualTo(treatmentCreated3.name());
        assertThat(persisted.get(1).getDescription()).isEqualTo(treatmentCreated3.description());
    }
}
