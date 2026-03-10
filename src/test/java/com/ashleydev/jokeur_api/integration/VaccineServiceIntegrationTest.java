package com.ashleydev.jokeur_api.integration;

import com.ashleydev.jokeur_api.domain.enums.ReminderStatus;
import com.ashleydev.jokeur_api.domain.enums.pets.AnimalType;
import com.ashleydev.jokeur_api.domain.enums.pets.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.pets.PetColor;
import com.ashleydev.jokeur_api.domain.enums.pets.PetSex;
import com.ashleydev.jokeur_api.domain.services.HealthRecordService;
import com.ashleydev.jokeur_api.domain.services.VaccineService;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordResponseDto;
import com.ashleydev.jokeur_api.exposition.dtos.reminder.ReminderVaccineRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.vaccine.VaccineDetailRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.vaccine.VaccineRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.vaccine.VaccineResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import com.ashleydev.jokeur_api.persistence.entities.VaccineEntity;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.vaccine.VaccineRepository;
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
public class VaccineServiceIntegrationTest {
    @Autowired
    private VaccineService vaccineService;

    @Autowired
    private HealthRecordService healthRecordService;

    @Autowired private VaccineRepository vaccineRepository;
    @Autowired private HealthRecordRepository healthRecordRepository;
    @Autowired private UserRepository userRepository;

    private Long userId;
    private HealthRecordResponseDto healthRecord;

    private VaccineRequestDto vaccine1;
    private VaccineRequestDto vaccine2;
    private VaccineRequestDto vaccine3;
    private VaccineRequestDto vaccine4;

    private VaccineResponseDto vaccineCreated1;
    private VaccineResponseDto vaccineCreated2;
    private VaccineResponseDto vaccineCreated3;
    private  VaccineResponseDto vaccineCreated4;



    @BeforeEach
    void setUp() {
        healthRecordRepository.deleteAll();
        userRepository.deleteAll();
        vaccineRepository.deleteAll();

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

        vaccine1 = new VaccineRequestDto("Naccie", "Vaccin contre la rage", "Dr Dumas", LocalDate.now(), LocalDate.of(2026, 12, 12), healthRecord.id());
        vaccine2 = new VaccineRequestDto("Fizer", "Vaccin contre la polio", "Dr Dumas", LocalDate.now(), LocalDate.of(2026, 12, 12), healthRecord.id());
        vaccine3 = new VaccineRequestDto("Props", "Vaccin contre ebola", "Dr Dumas", LocalDate.now(), LocalDate.of(2026, 12, 12), healthRecord.id());
        vaccine4 = new VaccineRequestDto("Stacy", "Vaccin contre la grippe", "Dr Dumas", LocalDate.now(), LocalDate.of(2026, 12, 12), healthRecord.id());

        vaccineCreated1 = vaccineService.create(vaccine1);
        vaccineCreated2 = vaccineService.create(vaccine2);
        vaccineCreated3 = vaccineService.create(vaccine3);
        vaccineCreated4 = vaccineService.create(vaccine4);
    }

    @Test
    @Transactional
    void shouldCreateVaccines() {
        List<VaccineEntity> allVaccine = vaccineRepository.findAllByHealthRecordI(healthRecord.id());

        assertThat(allVaccine).hasSize(4);

        VaccineEntity persisted = allVaccine.get(2);
        assertThat(persisted.getId()).isEqualTo(vaccineCreated3.id());
        assertThat(persisted.getCreationDate()).isEqualTo(LocalDate.now());
        assertThat(persisted.getReminderEntity().getReminderDate()).isEqualTo(LocalDate.of(2026, 12, 12));
        assertThat(persisted.getVaccinator()).isEqualTo(vaccine3.vaccinator());
        assertThat(persisted.getName()).isEqualTo(vaccine3.name());

        assertThat(persisted.getReminderEntity().getDescription()).isEqualTo("Rappel pour le vaccin : " +vaccine3.name() + " de " + healthRecord.petName());
        assertThat(persisted.getReminderEntity().getUser().getId()).isEqualTo(healthRecord.userId());
    }

    @Test
    @Transactional
    void shouldThrowForBadCreation() {
        VaccineRequestDto vaccineInvalid = new VaccineRequestDto("Naccie", "Vaccin contre la rage", "Dr Dumas", LocalDate.now(), LocalDate.now(), 999L);

        Exception exception1 = assertThrows(RuntimeException.class,
                () -> vaccineService.create(vaccineInvalid));

        assertTrue(exception1.getMessage().contains("Health record not found with id: 999"));
    }

    @Test
    @Transactional
    void shouldGetVaccinesByHealthRecord() {
        List<VaccineEntity> allVaccine = vaccineRepository.findAllByHealthRecordI(healthRecord.id());
        List<VaccineResponseDto> response = vaccineService.getAllByHealthRecordId(healthRecord.id());

        assertThat(allVaccine).hasSize(response.size());

        VaccineEntity persisted = allVaccine.get(2);
        VaccineResponseDto test = response.get(2);
        assertThat(persisted.getId()).isEqualTo(test.id());
        assertThat(persisted.getName()).isEqualTo(test.name());
    }

    @Test
    @Transactional
    void shouldThrowForBadRequestGetVaccineByHealthRecord() {
        Long invalidhealthRecordId = 999L;
        Exception exception1 = assertThrows(RuntimeException.class,
                () -> vaccineService.getAllByHealthRecordId(invalidhealthRecordId));

        assertTrue(exception1.getMessage().contains("Health record not found with id: 999"));
    }

    @Test
    @Transactional
    void shouldGetVaccinesById() {
        VaccineEntity vaccinePersisted = vaccineRepository.findById(vaccineCreated1.id()).get();
        VaccineResponseDto response = vaccineService.getById(vaccineCreated1.id());

        assertThat(vaccinePersisted.getId()).isEqualTo(response.id());
        assertThat(vaccinePersisted.getName()).isEqualTo(response.name());
    }

    @Test
    @Transactional
    void shouldThrowForBadRequestGetMeasure() {
        Long invalidId = 999L;
        Exception exception1 = assertThrows(RuntimeException.class,
                () -> vaccineService.getById(invalidId));

        assertTrue(exception1.getMessage().contains("Vaccine not found with id: " + invalidId));
    }

    @Test
    @Transactional
    void shouldGetUpdate() {
        VaccineEntity persisted = vaccineRepository.findById(vaccineCreated1.id()).get();
        ReminderVaccineRequestDto newReminder = new ReminderVaccineRequestDto(vaccineCreated1.reminder().id(), "new description for reminder", LocalDate.of(2026, 6, 6), ReminderStatus.PENDING);
        VaccineDetailRequestDto request = new VaccineDetailRequestDto(vaccineCreated1.id(), "Tester", "new desciption", "Testeur", LocalDate.of(2007, 12, 5), healthRecord.id(), newReminder);

        vaccineService.update(request);
        VaccineResponseDto test = vaccineService.getById(vaccineCreated1.id());

        assertThat(test.id()).isEqualTo(persisted.getId());
        assertThat(test.description()).isEqualTo(request.description());
        assertThat(test.reminder().reminderDate()).isEqualTo(newReminder.reminderDate());
        assertThat(test.name()).isEqualTo(request.name());
    }

    @Test
    @Transactional
    void shouldThrowForBadUpdateRequest() {
        VaccineEntity persisted = vaccineRepository.findById(vaccineCreated1.id()).get();
        ReminderVaccineRequestDto newReminder = new ReminderVaccineRequestDto(1L, "new description for reminder", LocalDate.of(2026, 6, 6), ReminderStatus.PENDING);
        VaccineDetailRequestDto invalidRequest1 = new VaccineDetailRequestDto(999L, "Tester", "new desciption", "Testeur", LocalDate.of(2007, 12, 5), healthRecord.id(), newReminder);
        VaccineDetailRequestDto invalidRequest2 = new VaccineDetailRequestDto(1L, "Tester", "new desciption", "Testeur", LocalDate.of(2007, 12, 5), 999L, newReminder);


        Exception ex = assertThrows(RuntimeException.class,
                () -> vaccineService.update(invalidRequest1));

        assertTrue(ex.getMessage().contains("Vaccine not found with id: 999"));

        Exception ex2 = assertThrows(RuntimeException.class,
                () -> vaccineService.update(invalidRequest2));

        assertTrue(ex2.getMessage().contains("Health record not found with id: 999"));
    }

    @Test
    @Transactional
    void shouldGetDelete() {
        List<VaccineEntity> valueToTest = vaccineRepository.findAll();
        String response = vaccineService.delete(vaccineCreated2.id(), healthRecord.id());
        List<VaccineEntity> persisted = vaccineRepository.findAll();

        assertThat(response).isEqualTo("Vaccine : " + vaccineCreated2.id() + " is deleted");

        assertThat(persisted.size()).isEqualTo(valueToTest.size()-1);
        assertThat(persisted.get(1).getName()).isEqualTo(vaccineCreated3.name());
        assertThat(persisted.get(1).getDescription()).isEqualTo(vaccineCreated3.description());
    }
}
