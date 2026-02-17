package com.ashleydev.jokeur_api.unit;

import com.ashleydev.jokeur_api.domain.rules.MeasureRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MeasureRulesTest {
    private String[] validType;
    private String invalidType;

    @BeforeEach
    void setup() {
        // Arrange
        validType = new String[]{"weight", "Weight", "WEIGHT", "bpm", "Bpm", "BPM", "temperature", "Temperature", "TEMPERATURE", "respiratory_rate", "Respiratory_Rate", "respiratory_Rate", "RESPIRATORY_RATE"};
        invalidType = "test";
    }

    @Test
    @DisplayName("Should throw if type is invalid")
    void shouldThrowIfTypeIsInvald() {
        Exception ex = assertThrows(RuntimeException.class,
                () -> MeasureRules.validateType(invalidType));

        assertTrue(ex.getMessage().contains("Type is not valid"));
    }

    @Test
    @DisplayName("Should pass if type is valid")
    void shouldPassIfTypeIsValid() {
        for (String type : validType) {
            assertDoesNotThrow(() -> MeasureRules.validateType(type));
        }
    }
}
