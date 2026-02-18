package com.ashleydev.jokeur_api.integration;

import com.ashleydev.jokeur_api.exposition.controllers.FormOptionsController;
import com.ashleydev.jokeur_api.exposition.dtos.formOption.FormOptionsResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.profiles.active=integration")
@ActiveProfiles("integration")
@Transactional
class FormOptionsControllerIntegrationTest {

    private final FormOptionsController formOptionsController;

    @Autowired
    FormOptionsControllerIntegrationTest(FormOptionsController formOptionsController) {
        this.formOptionsController = formOptionsController;
    }

    @Test
    void getFormOptions_shouldReturnAllOptions() {
        FormOptionsResponseDTO response = formOptionsController.getFormOptions();

        assertThat(response).isNotNull();
        assertThat(response.animalTypes()).hasSize(2);
        assertThat(response.sexes()).hasSize(3);
        assertThat(response.colors()).hasSize(6);
        assertThat(response.breedsByAnimalType()).containsKeys("DOG", "CAT");
        assertThat(response.breedsByAnimalType().get("DOG")).isNotEmpty();
        assertThat(response.breedsByAnimalType().get("CAT")).isNotEmpty();
    }
}