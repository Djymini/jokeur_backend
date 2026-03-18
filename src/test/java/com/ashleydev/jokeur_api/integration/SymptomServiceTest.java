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
                symptomService.createSymptom("mal aux poil");

        assertNotNull(response);

        assertTrue(
                symptomRepository.existsByNameIgnoreCase("mal aux poil")
        );
    }

    @Test
    void getAllSymptoms_shouldReturnAllSymptoms() {

        symptomRepository.save(new SymptomEntity(null, "nouvel symptôme", new ArrayList<>()));
        symptomRepository.save(new SymptomEntity(null, "mal aux ongles", new ArrayList<>()));

        List<SymptomResponseDTO> symtoms = symptomService.getAllSymptoms();
        assertEquals(2,symtoms.size());
    }

}
