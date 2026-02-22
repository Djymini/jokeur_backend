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
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;

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
    private final UserRepository userRepository;
    private final HealthRecordRepository healthRecordRepository;

    private Long userId;

    @Autowired
    HealthRecordServiceIntegrationTest(
            HealthRecordService healthRecordService,
            UserRepository userRepository,
            HealthRecordRepository healthRecordRepository
    ) {
        this.healthRecordService = healthRecordService;
        this.userRepository = userRepository;
        this.healthRecordRepository = healthRecordRepository;
    }

    @BeforeEach
    void setUp() {
        healthRecordRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity user = new UserEntity();
        user.setEmail("owner@test.com");
        user.setFirstname("Owner");
        user.setName("Test");
        user.setPhoneNumber("0600000000");
        user.setPassword("P@ssword1234");
        userId = userRepository.save(user).getId();
    }

    @Test
    void create_shouldPersist_andReturnResponse() {
        HealthRecordRequestDTO dto = validCreateDto(userId);

        HealthRecordResponseDto saved = healthRecordService.create(dto);

        assertNotNull(saved.id());
        assertEquals(userId, saved.userId());
        assertEquals("Naya", saved.petName());

        assertTrue(healthRecordRepository.existsById(saved.id()));
    }

    @Test
    void create_shouldThrow_whenUserNotFound() {
        HealthRecordRequestDTO dto = validCreateDto(999999L);

        assertThrows(OwnerNotFoundException.class, () -> healthRecordService.create(dto));
    }

    @Test
    void getByHealthRecordId_shouldReturnData_whenExists() {
        HealthRecordResponseDto created = healthRecordService.create(validCreateDto(userId));

        HealthRecordResponseDto found =
                healthRecordService.getByHealthRecordId(created.id());

        assertEquals(created.id(), found.id());
        assertEquals(userId, found.userId());
        assertEquals("Naya", found.petName());
    }

    @Test
    void getDashboardByUser_shouldReturnDashboardDtos() {
        healthRecordService.create(validCreateDto(userId, "Naya"));
        healthRecordService.create(validCreateDto(userId, "Milo"));

        var dashboard = healthRecordService.getDashboardByUser(userId);

        assertNotNull(dashboard);
        assertEquals(2, dashboard.size());

        assertTrue(dashboard.stream().allMatch(d -> d.id() != null));
        assertTrue(dashboard.stream().allMatch(d -> d.petName() != null && !d.petName().isBlank()));

        var names = dashboard.stream().map(d -> d.petName()).toList();
        assertTrue(names.contains("Naya"));
        assertTrue(names.contains("Milo"));
    }

    @Test
    void getMyAnimalsByUser_shouldReturnMyAnimalsDtos() {
        healthRecordService.create(validCreateDto(userId, "Naya"));
        healthRecordService.create(validCreateDto(userId, "Milo"));

        var myAnimals = healthRecordService.getMyAnimalsByUser(userId);

        assertNotNull(myAnimals);
        assertEquals(2, myAnimals.size());

        assertTrue(myAnimals.stream().allMatch(a -> a.id() != null));
        assertTrue(myAnimals.stream().allMatch(a -> a.petName() != null && !a.petName().isBlank()));
        assertTrue(myAnimals.stream().allMatch(a -> a.animalType() != null));
        assertTrue(myAnimals.stream().allMatch(a -> a.sex() != null));
    }

    private HealthRecordRequestDTO validCreateDto(Long userId) {
        return new HealthRecordRequestDTO(
                userId,
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

    private HealthRecordRequestDTO validCreateDto(Long userId, String petName) {
        return new HealthRecordRequestDTO(
                userId,
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
