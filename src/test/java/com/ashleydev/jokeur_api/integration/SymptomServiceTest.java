package com.ashleydev.jokeur_api.integration;

import com.ashleydev.jokeur_api.domain.services.SymptomService;
import com.ashleydev.jokeur_api.exposition.dtos.symptom.SymptomResponseDTO;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class SymptomServiceTest {

    @Autowired
    private SymptomService symptomService;

    @Autowired
    private SymptomRepository symptomRepository;

    @Test
    void createSymptom_shouldSaveSymptom() {

        SymptomResponseDTO response =
                symptomService.createSymptom("Diarrhée");

        assertNotNull(response);

        assertTrue(
                symptomRepository.existsByNameIgnoreCase("Diarrhée")
        );
    }

}
