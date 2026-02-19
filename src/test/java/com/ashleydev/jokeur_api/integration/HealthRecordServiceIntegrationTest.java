package com.ashleydev.jokeur_api.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.ashleydev.jokeur_api.domain.enums.pets.AnimalType;
import com.ashleydev.jokeur_api.domain.enums.pets.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.pets.PetColor;
import com.ashleydev.jokeur_api.domain.enums.pets.PetSex;
import com.ashleydev.jokeur_api.domain.services.HealthRecordService;
import com.ashleydev.jokeur_api.exceptions.owner.OwnerNotFoundException;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.OwnerEntity;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
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

    private Long ownerId;

    @Autowired
    HealthRecordServiceIntegrationTest(
            HealthRecordService healthRecordService,
            OwnerRepository ownerRepository,
            HealthRecordRepository healthRecordRepository
    ) {
        this.healthRecordService = healthRecordService;
        this.ownerRepository = ownerRepository;
        this.healthRecordRepository = healthRecordRepository;
    }

    @BeforeEach
    void setUp() {
        healthRecordRepository.deleteAll();
        ownerRepository.deleteAll();

        OwnerEntity owner = new OwnerEntity();
        owner.setEmail("owner@test.com");
        owner.setName("Owner Test");
        owner.setPhoneNumber("0600000000");
        ownerId = ownerRepository.save(owner).getId();
    }

    @Test
    void create_shouldPersist_andReturnResponse() {
        HealthRecordRequestDTO dto = validCreateDto(ownerId);

        HealthRecordResponseDto saved = healthRecordService.create(dto);

        assertNotNull(saved.id());
        assertEquals(ownerId, saved.id());
        assertEquals("Naya", saved.petName());

        assertTrue(healthRecordRepository.existsById(saved.id()));
    }

    @Test
    void create_shouldThrow_whenOwnerNotFound() {
        HealthRecordRequestDTO dto = validCreateDto(999999L);

        assertThrows(OwnerNotFoundException.class, () -> healthRecordService.create(dto));
    }

    @Test
    void getByHealthRecordId_shouldReturnData_whenExists() {
        HealthRecordResponseDto created = healthRecordService.create(validCreateDto(ownerId));

        HealthRecordResponseDto found =
                healthRecordService.getByHealthRecordId(created.id());

        assertEquals(created.id(), found.id());
        assertEquals(ownerId, found.id());
        assertEquals("Naya", found.petName());
    }

    @Test
    void getDashboardByOwner_shouldReturnDashboardDtos() {
        healthRecordService.create(validCreateDto(ownerId, "Naya"));
        healthRecordService.create(validCreateDto(ownerId, "Milo"));

        var dashboard = healthRecordService.getDashboardByOwner(ownerId);

        assertNotNull(dashboard);
        assertEquals(2, dashboard.size());

        assertTrue(dashboard.stream().allMatch(d -> d.id() != null));
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

        assertTrue(myAnimals.stream().allMatch(a -> a.id() != null));
        assertTrue(myAnimals.stream().allMatch(a -> a.petName() != null && !a.petName().isBlank()));
        assertTrue(myAnimals.stream().allMatch(a -> a.animalType() != null));
        assertTrue(myAnimals.stream().allMatch(a -> a.sex() != null));
    }

    private HealthRecordRequestDTO validCreateDto(Long ownerId) {
        return new HealthRecordRequestDTO(
                ownerId,
                "Naya",
                AnimalType.values()[0],
                PetBreed.LABRADOR,
                PetSex.values()[0],
                LocalDate.now().minusYears(2),
                new BigDecimal("4.20"),
                PetColor.BLACK,
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
                PetBreed.PERSIAN,
                PetSex.values()[0],
                LocalDate.now().minusYears(2),
                new BigDecimal("4.20"),
                PetColor.BLACK,
                "CHIP-" + petName,
                null,
                null
        );
    }
}
