package com.ashleydev.jokeur_api.unit;

import static org.junit.jupiter.api.Assertions.*;

import com.ashleydev.jokeur_api.domain.enums.pets.AnimalType;
import com.ashleydev.jokeur_api.domain.enums.pets.PetSex;
import com.ashleydev.jokeur_api.domain.rules.HealthRecordRules;
import com.ashleydev.jokeur_api.exceptions.healthRecord.HealthRecordValidationException;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordRequestDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HealthRecordRulesCreateTest {

    private HealthRecordRules rules;

    @BeforeEach
    void setUp() {
        rules = new HealthRecordRules(null);
    }

    @Test
    void validateCreate_shouldThrow_whenDtoIsNull() {
        assertThrows(HealthRecordValidationException.class, () -> rules.validateCreate(null));
    }

    @Test
    void validateCreate_shouldThrow_whenUserIdIsNull() {
        HealthRecordRequestDTO dto = createDto(
                null, "Naya", AnimalType.values()[0], PetSex.values()[0],
                LocalDate.now().minusYears(2), new BigDecimal("4.2"),
                "CHIP123", null, null
        );

        HealthRecordValidationException ex =
                assertThrows(HealthRecordValidationException.class, () -> rules.validateCreate(dto));

        assertEquals("userId is required.", ex.getMessage());
    }

    @Test
    void validateCreate_shouldThrow_whenPetNameIsBlank() {
        HealthRecordRequestDTO dto = createDto(
                1L, "   ", AnimalType.values()[0], PetSex.values()[0],
                LocalDate.now().minusYears(2), new BigDecimal("4.2"),
                "CHIP123", null, null
        );

        HealthRecordValidationException ex =
                assertThrows(HealthRecordValidationException.class, () -> rules.validateCreate(dto));

        assertEquals("petName is required.", ex.getMessage());
    }

    @Test
    void validateCreate_shouldThrow_whenAnimalTypeIsNull() {
        HealthRecordRequestDTO dto = createDto(
                1L, "Naya", null, PetSex.values()[0],
                LocalDate.now().minusYears(2), new BigDecimal("4.2"),
                "CHIP123", null, null
        );

        HealthRecordValidationException ex =
                assertThrows(HealthRecordValidationException.class, () -> rules.validateCreate(dto));

        assertEquals("animalType is required.", ex.getMessage());
    }

    @Test
    void validateCreate_shouldThrow_whenSexIsNull() {
        HealthRecordRequestDTO dto = createDto(
                1L, "Naya", AnimalType.values()[0], null,
                LocalDate.now().minusYears(2), new BigDecimal("4.2"),
                "CHIP123", null, null
        );

        HealthRecordValidationException ex =
                assertThrows(HealthRecordValidationException.class, () -> rules.validateCreate(dto));

        assertEquals("sex is required.", ex.getMessage());
    }

    @Test
    void validateCreate_shouldThrow_whenBirthDateIsInFuture() {
        HealthRecordRequestDTO dto = createDto(
                1L, "Naya", AnimalType.values()[0], PetSex.values()[0],
                LocalDate.now().plusDays(1), new BigDecimal("4.2"),
                "CHIP123", null, null
        );

        HealthRecordValidationException ex =
                assertThrows(HealthRecordValidationException.class, () -> rules.validateCreate(dto));

        assertEquals("birthDate cannot be in the future.", ex.getMessage());
    }

    @Test
    void validateCreate_shouldThrow_whenWeightIsZero() {
        HealthRecordRequestDTO dto = createDto(
                1L, "Naya", AnimalType.values()[0], PetSex.values()[0],
                LocalDate.now().minusYears(2), BigDecimal.ZERO,
                "CHIP123", null, null
        );

        HealthRecordValidationException ex =
                assertThrows(HealthRecordValidationException.class, () -> rules.validateCreate(dto));

        assertEquals("currentWeight must be > 0.", ex.getMessage());
    }

    @Test
    void validateCreate_shouldThrow_whenIdentificationNumberIsBlank() {
        HealthRecordRequestDTO dto = createDto(
                1L, "Naya", AnimalType.values()[0], PetSex.values()[0],
                LocalDate.now().minusYears(2), new BigDecimal("4.2"),
                " ", null, null
        );

        HealthRecordValidationException ex =
                assertThrows(HealthRecordValidationException.class, () -> rules.validateCreate(dto));

        assertEquals("identificationNumber cannot be blank.", ex.getMessage());
    }

    @Test
    void validateCreate_shouldThrow_whenTattooNumberIsBlank() {
        HealthRecordRequestDTO dto = createDto(
                1L, "Naya", AnimalType.values()[0], PetSex.values()[0],
                LocalDate.now().minusYears(2), new BigDecimal("4.2"),
                "CHIP123", " ", null
        );

        HealthRecordValidationException ex =
                assertThrows(HealthRecordValidationException.class, () -> rules.validateCreate(dto));

        assertEquals("tattooNumber cannot be blank.", ex.getMessage());
    }

    @Test
    void validateCreate_shouldThrow_whenAllergyIsBlank() {
        HealthRecordRequestDTO dto = createDto(
                1L, "Naya", AnimalType.values()[0], PetSex.values()[0],
                LocalDate.now().minusYears(2), new BigDecimal("4.2"),
                "CHIP123", null, " "
        );

        HealthRecordValidationException ex =
                assertThrows(HealthRecordValidationException.class, () -> rules.validateCreate(dto));

        assertEquals("allergy cannot be blank.", ex.getMessage());
    }

    @Test
    void validateCreate_shouldPass_whenDtoIsValid() {
        HealthRecordRequestDTO dto = createDto(
                1L, "Naya", AnimalType.values()[0], PetSex.values()[0],
                LocalDate.now().minusYears(2), new BigDecimal("4.2"),
                "CHIP123", null, null
        );

        assertDoesNotThrow(() -> rules.validateCreate(dto));
    }

    @Test
    void validateCreate_shouldThrow_whenPetNameIsNull() {
        HealthRecordRequestDTO dto = createDto(
                1L, null, AnimalType.values()[0], PetSex.values()[0],
                LocalDate.now().minusYears(2), new BigDecimal("4.2"),
                "CHIP123", null, null
        );

        HealthRecordValidationException ex =
                assertThrows(HealthRecordValidationException.class, () -> rules.validateCreate(dto));

        assertEquals("petName is required.", ex.getMessage());
    }

    @Test
    void validateCreate_shouldPass_whenWeightIsNull() {
        HealthRecordRequestDTO dto = createDto(
                1L, "Naya", AnimalType.values()[0], PetSex.values()[0],
                LocalDate.now().minusYears(2), null,
                "CHIP123", null, null
        );

        assertDoesNotThrow(() -> rules.validateCreate(dto));
    }


    private HealthRecordRequestDTO createDto(
            Long userId,
            String petName,
            AnimalType animalType,
            PetSex sex,
            LocalDate birthDate,
            BigDecimal weight,
            String identificationNumber,
            String tattooNumber,
            String allergy
    ) {
        return new HealthRecordRequestDTO(
                userId,
                petName,
                animalType,
                null,
                sex,
                birthDate,
                weight,
                null,
                identificationNumber,
                tattooNumber,
                allergy
        );
    }
}
