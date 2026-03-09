package com.ashleydev.jokeur_api.unit;

import com.ashleydev.jokeur_api.domain.rules.SymptomRules;
import com.ashleydev.jokeur_api.exceptions.symptom.BusinessException;
import com.ashleydev.jokeur_api.persistence.entities.SymptomEntity;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class SymptomRulesTest {

    @Autowired
    private SymptomRules symptomRules;

    @Autowired
    private SymptomRepository symptomRepository;

    @Test
    void checkExists_shouldThrowException_whenSymptomNotFound() {

        Long id = 79L;

        assertThrows(
                BusinessException.class,
                () -> symptomRules.checkExists(id)
        );
    }

    @Test
    void checkExists_shouldPass_whenSymptomExists() {

        SymptomEntity symptom = new SymptomEntity();
        symptom.setName("Vomissements");

        symptomRepository.save(symptom);

        assertDoesNotThrow(
                () -> symptomRules.checkExists(symptom.getId())
        );
    }

    @Test
    void checkNameNotExists_shouldThrowException_whenNameAlreadyExist(){
        SymptomEntity symptom = new SymptomEntity();
        symptom.setName("Fièvre");
        symptomRepository.save(symptom);

        assertThrows(
                BusinessException.class, ()->symptomRules.checkNameNotExists("Fièvre")
        );
    }

}
