package com.ashleydev.jokeur_api.integration;

import com.ashleydev.jokeur_api.domain.services.SymptomService;
import com.ashleydev.jokeur_api.exposition.dtos.symptom.SymptomResponseDTO;
import com.ashleydev.jokeur_api.persistence.entities.SymptomEntity;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active=integration")
@ActiveProfiles("integration")
@Transactional
class SymptomServiceTest {

    @Autowired
    private SymptomService symptomService;

    @Autowired
    private SymptomRepository symptomRepository;


    @Test
    void createSymptom_shouldSaveSymptom() {

        SymptomResponseDTO response =
                symptomService.createSymptom("new symptom");

        assertNotNull(response);

        assertTrue(
                symptomRepository.existsByNameIgnoreCase("new symptom")
        );
    }

    @Test
    void getAllSymptoms_shouldReturnAllSymptoms() {
        List<SymptomEntity> persisted = symptomRepository.findAll();
        List<SymptomResponseDTO> symtoms = symptomService.getAllSymptoms();

        assertEquals(persisted.size(),symtoms.size());
    }

}
