package com.ashleydev.jokeur_api.unit;

import com.ashleydev.jokeur_api.domain.rules.SymptomRules;
import com.ashleydev.jokeur_api.exceptions.symptom.BusinessException;
import com.ashleydev.jokeur_api.exceptions.symptom.SymptomNotFoundException;
import com.ashleydev.jokeur_api.persistence.entities.SymptomEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SymptomRulesTest {

    private SymptomEntity symptom;


    @BeforeEach
    void setup() {
        // Arrange
        symptom = new SymptomEntity();
        symptom.setId(1L);
        symptom.setName("Toux sec");
    }

    @Test
    @DisplayName("Should throw if symptom does not exist")
    void shouldThrowIfSymptomNotExists() {

        Exception ex = assertThrows(
                SymptomNotFoundException.class,
                () -> SymptomRules.validateExists(null)
        );

        assertFalse(ex.getMessage().contains("Symptom non trouvé"));
    }

    @Test
    @DisplayName("Should pass if symptom exists")
    void shouldPassIfSymptomExists() {
        assertDoesNotThrow(
                () -> SymptomRules.validateExists(symptom)
        );
    }

    @Test
    @DisplayName("Should throw if name already exists")
    void shouldThrowIfNameAlreadyExists() {

    Exception ex = assertThrows(
            BusinessException.class,
            ()-> SymptomRules.validateNameNoteExists(true, "Toux sec")
    );
    assertTrue(ex.getMessage().contains("existe déjà"));
    }

    @Test
    @DisplayName("Should pass if name does not exist")
    void shouldPassIfNameNotExist5(){
        assertDoesNotThrow(
                ()-> SymptomRules.validateNameNoteExists(false, "Toux sec")
        );
    }

    @Test
    @DisplayName("Should throw if symptom is used")
    void shouldThrowIfSymptomUsed(){
        Exception ex = assertThrows(
                BusinessException.class,
                () -> SymptomRules.validateNotUsed(true)
        );
        assertTrue(ex.getMessage().contains("Impossible de supprimer"));
    }

    @Test
    @DisplayName("Should pass if symptom is not used")
    void shouldPassIfSymptomNotUsed(){
        assertDoesNotThrow(
                ()-> SymptomRules.validateNotUsed(false)
        );
    }

}