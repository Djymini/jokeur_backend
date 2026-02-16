package com.ashleydev.jokeur_api.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.ashleydev.jokeur_api.domain.enums.AnimalType;
import com.ashleydev.jokeur_api.domain.enums.PetSex;
import com.ashleydev.jokeur_api.domain.services.HealthRecordService;
import com.ashleydev.jokeur_api.exceptions.owner.OwnerNotFoundException;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordResponseDTO;
import com.ashleydev.jokeur_api.persistence.entities.OwnerEntity;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.measure.MeasureRepository;
import com.ashleydev.jokeur_api.persistence.repositories.owner.OwnerRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = "spring.profiles.active=integration")
@ActiveProfiles("integration")
@Transactional
class HealthRecordServiceIntegrationTest {

    private final HealthRecordService healthRecordService;
    private final OwnerRepository ownerRepository;
    private final HealthRecordRepository healthRecordRepository;
    private final MeasureRepository measureRepository;

    private Long ownerId;

    @Autowired
    HealthRecordServiceIntegrationTest(
            HealthRecordService healthRecordService,
            OwnerRepository ownerRepository,
            HealthRecordRepository healthRecordRepository,
            MeasureRepository measureRepository
    ) {
        this.healthRecordService = healthRecordService;
        this.ownerRepository = ownerRepository;
        this.healthRecordRepository = healthRecordRepository;
        this.measureRepository = measureRepository;
    }

    @BeforeEach
    void setUp() {
        healthRecordRepository.deleteAll();
        ownerRepository.deleteAll();

        OwnerEntity owner = new OwnerEntity();
        owner.setEmail("owner@test.com");
        owner.setName("Owner Test");
        owner.setPhoneNumber("0600000000");
        ownerId = ownerRepository.save(owner).getIdOwner();
    }

    @Test
    void create_shouldPersist_andReturnResponse() {
        HealthRecordRequestDTO dto = validCreateDto(ownerId);

        HealthRecordResponseDTO saved = healthRecordService.create(dto);

        assertNotNull(saved.healthRecordNumber());
        assertEquals(ownerId, saved.ownerId());
        assertEquals("Naya", saved.petName());

        assertTrue(healthRecordRepository.existsByHealthRecordNumber(saved.healthRecordNumber()));
    }

    @Test
    void create_shouldThrow_whenOwnerNotFound() {
        HealthRecordRequestDTO dto = validCreateDto(999999L);

        assertThrows(OwnerNotFoundException.class, () -> healthRecordService.create(dto));
    }

    @Test
    void getByHealthRecordNumber_shouldReturnData_whenExists() {
        HealthRecordResponseDTO created = healthRecordService.create(validCreateDto(ownerId));

        HealthRecordResponseDTO found =
                healthRecordService.getByHealthRecordNumber(created.healthRecordNumber());

        assertEquals(created.healthRecordNumber(), found.healthRecordNumber());
        assertEquals(ownerId, found.ownerId());
        assertEquals("Naya", found.petName());
    }

    @Test
    void getDashboardByOwner_shouldReturnDashboardDtos() {
        healthRecordService.create(validCreateDto(ownerId, "Naya"));
        healthRecordService.create(validCreateDto(ownerId, "Milo"));

        var dashboard = healthRecordService.getDashboardByOwner(ownerId);

        assertNotNull(dashboard);
        assertEquals(2, dashboard.size());

        assertTrue(dashboard.stream().allMatch(d -> d.healthRecordNumber() != null));
        assertTrue(dashboard.stream().allMatch(d -> d.petName() != null && !d.petName().isBlank()));

        var names = dashboard.stream().map(d -> d.petName()).toList();
        assertTrue(names.contains("Naya"));
        assertTrue(names.contains("Milo"));
    }

    @Test
    void getMyAnimalsByOwner_shouldReturnMyAnimalsDtos() {
        healthRecordService.create(validCreateDto(ownerId, "Naya"));
        healthRecordService.create(validCreateDto(ownerId, "Milo"));

        var myAnimals = healthRecordService.getMyAnimalsByOwner(ownerId);

        assertNotNull(myAnimals);
        assertEquals(2, myAnimals.size());

        assertTrue(myAnimals.stream().allMatch(a -> a.healthRecordNumber() != null));
        assertTrue(myAnimals.stream().allMatch(a -> a.petName() != null && !a.petName().isBlank()));
        assertTrue(myAnimals.stream().allMatch(a -> a.animalType() != null));
        assertTrue(myAnimals.stream().allMatch(a -> a.sex() != null));
    }

    private HealthRecordRequestDTO validCreateDto(Long ownerId) {
        return new HealthRecordRequestDTO(
                ownerId,
                "Naya",
                AnimalType.values()[0],
                null,
                PetSex.values()[0],
                LocalDate.now().minusYears(2),
                new BigDecimal("4.20"),
                null,
                "CHIP123",
                null,
                null
        );
    }

    private HealthRecordRequestDTO validCreateDto(Long ownerId, String petName) {
        return new HealthRecordRequestDTO(
                ownerId,
                petName,
                AnimalType.values()[0],
                null,
                PetSex.values()[0],
                LocalDate.now().minusYears(2),
                new BigDecimal("4.20"),
                null,
                "CHIP-" + petName,
                null,
                null
        );
    }
}
