package com.ashleydev.jokeur_api.unit;

import com.ashleydev.jokeur_api.domain.rules.MeasureRules;
import com.ashleydev.jokeur_api.domain.rules.VaccineRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VaccineRulesTest {
    private String vaccineName;

    @BeforeEach
    void setup() {
        vaccineName = "Test";
    }

    @Test
    @DisplayName("Should return the good description")
    void shouldThrowIfTypeIsInvald() {
        String test = VaccineRules.formatReminderVaccineDescription(vaccineName);

        assertThat(test).isEqualTo("Rappel pour le vaccin : " +vaccineName);
    }
}
