package com.ashleydev.jokeur_api.unit;

import com.ashleydev.jokeur_api.domain.rules.SymptomRules;
import com.ashleydev.jokeur_api.exceptions.symptom.BusinessException;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomHealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SymptomRulesTest {

    @InjectMocks
    private SymptomRules symptomRules;

    @Mock
    private SymptomRepository symptomRepository;
    @Mock
    private SymptomHealthRecordRepository symptomHealthRecordRepository;


    @Test
    void checkExists_shouldThrowException_whenSymptomNotFound() {
        Mockito.when(symptomRepository.existsById(Mockito.anyLong())).thenReturn(false);
        Long id = 1L;
        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> symptomRules.checkExists(id)
        );

        assertTrue(ex.getMessage().contains("Symptom non trouvé"));
        verify(symptomRepository).existsById(id);
    }


    @Test
    void checkExists_shouldPass_whenSymptomExists() {

        Long id = 5L;
        when(symptomRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(
                () -> symptomRules.checkExists(id)
        );
    }

    @Test
    void checkNameNotExists_shouldThrowException_whenNameAlreadyExist(){
        String fievre = "Fièvre";
        when(symptomRepository.existsByNameIgnoreCase(fievre)).thenReturn(true);
        assertThrows(
                BusinessException.class, ()->symptomRules.checkNameNotExists(fievre)
        );
    }

}
