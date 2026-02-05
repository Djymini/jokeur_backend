package com.ashleydev.jokeur_api.domain.rules;

import com.ashleydev.jokeur_api.exceptions.healthRecord.HealthRecordUpdateEmptyException;
import com.ashleydev.jokeur_api.exceptions.healthRecord.HealthRecordValidationException;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordUpdateDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class HealthRecordRules {

    public void validateCreate(HealthRecordRequestDTO dto) {
        if (dto == null) {
            throw new HealthRecordValidationException("HealthRecordRequestDTO is required.");
        }

        if (dto.ownerId() == null) {
            throw new HealthRecordValidationException("ownerId is required.");
        }

        if (dto.petName() == null || dto.petName().isBlank()) {
            throw new HealthRecordValidationException("petName is required.");
        }

        if (dto.animalType() == null) {
            throw new HealthRecordValidationException("animalType is required.");
        }

        if (dto.sex() == null) {
            throw new HealthRecordValidationException("sex is required.");
        }

        if (dto.birthDate() != null && dto.birthDate().isAfter(LocalDate.now())) {
            throw new HealthRecordValidationException("birthDate cannot be in the future.");
        }

        BigDecimal weight = dto.currentWeight();
        if (weight != null && weight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new HealthRecordValidationException("currentWeight must be > 0.");
        }

        if (dto.identificationNumber() != null && dto.identificationNumber().isBlank()) {
            throw new HealthRecordValidationException("identificationNumber cannot be blank.");
        }

        if (dto.tattooNumber() != null && dto.tattooNumber().isBlank()) {
            throw new HealthRecordValidationException("tattooNumber cannot be blank.");
        }

        if (dto.allergy() != null && dto.allergy().isBlank()) {
            throw new HealthRecordValidationException("allergy cannot be blank.");
        }
    }

    public void validateUpdate(HealthRecordUpdateDTO dto) {
        if (dto == null || !dto.hasAtLeastOneField()) {
            throw new HealthRecordUpdateEmptyException();
        }

        if (dto.getPetName() != null && dto.getPetName().isBlank()) {
            throw new HealthRecordValidationException("petName cannot be blank.");
        }

        if (dto.getBirthDate() != null && dto.getBirthDate().isAfter(LocalDate.now())) {
            throw new HealthRecordValidationException("birthDate cannot be in the future.");
        }

        BigDecimal weight = dto.getCurrentWeight();
        if (weight != null && weight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new HealthRecordValidationException("currentWeight must be > 0.");
        }

        if (dto.getIdentificationNumber() != null && dto.getIdentificationNumber().isBlank()) {
            throw new HealthRecordValidationException("identificationNumber cannot be blank.");
        }

        if (dto.getTattooNumber() != null && dto.getTattooNumber().isBlank()) {
            throw new HealthRecordValidationException("tattooNumber cannot be blank.");
        }

        if (dto.getAllergy() != null && dto.getAllergy().isBlank()) {
            throw new HealthRecordValidationException("allergy cannot be blank.");
        }
    }
}
