package com.ashleydev.jokeur_api.unit;

import com.ashleydev.jokeur_api.domain.rules.SymptomHealthRecordRules;
import com.ashleydev.jokeur_api.exceptions.symptom.InvalidSymptomDateException;
import com.ashleydev.jokeur_api.exceptions.symptom.SymptomNotActiveException;
import com.ashleydev.jokeur_api.persistence.entities.SymptomHealthRecordEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class SymptomHealthRecordRulesTest {

    private SymptomHealthRecordRules rules;

    @BeforeEach
    void setup() {
        // Arrange
        rules = new SymptomHealthRecordRules(null);
    }

    @Test
    @DisplayName("Should throw exception when end date is before observation date")
    void checkDateConsistency_shouldThrowException() {

        LocalDate observationDate = LocalDate.of(2025,1,10);
        LocalDate end = LocalDate.of(2025,1,5);

        Exception ex = assertThrows(
                InvalidSymptomDateException.class,
                () -> rules.checkDateConsistency(observationDate,end)
        );

        assertTrue(ex.getMessage().contains("date"));
    }

    @Test
    @DisplayName("Should throw exception if symptom is not active")
    void should_throw_exception_if_symptom_not_active(){

        SymptomHealthRecordEntity symptomHealthRecord = new SymptomHealthRecordEntity();
        symptomHealthRecord.setIsActive(false);

        Exception ex = assertThrows(
                SymptomNotActiveException.class,
                ()-> rules.checkSymptomIsActive(symptomHealthRecord)
        );

        assertTrue(ex.getMessage().contains("inactif"));
    }

    @Test
    @DisplayName("Should pass when symptom is active")
    void should_not_throw_when_symptom_is_active(){

        SymptomHealthRecordEntity symptomHealthRecord = new SymptomHealthRecordEntity();
        symptomHealthRecord.setIsActive(true);

        assertDoesNotThrow(
                ()-> rules.checkSymptomIsActive(symptomHealthRecord)
        );
    }

    @Test
    @DisplayName("Should pass when dates are valid")
    void should_not_throw_when_dates_are_valid(){

        LocalDate observationDate = LocalDate.of(2025,1,5);
        LocalDate end = LocalDate.of(2025,1,10);

        assertDoesNotThrow(
                () -> rules.checkDateConsistency(observationDate,end)
        );
    }
}