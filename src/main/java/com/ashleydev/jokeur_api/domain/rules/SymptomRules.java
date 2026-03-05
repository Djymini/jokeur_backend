package com.ashleydev.jokeur_api.domain.rules;


import com.ashleydev.jokeur_api.exceptions.symptom.BusinessException;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomHealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SymptomRules {

    private final SymptomRepository symptomRepository;
    private final SymptomHealthRecordRepository symptomHealthRecordRepository;

    public void checkExists(Long id) {
        if (!symptomRepository.existsById(id)) {
            throw new BusinessException("Symptom non trouvé avec id: " + id);
        }
    }

    public void checkNameNotExists(String name) {
        if (symptomRepository.existsByNameIgnoreCase(name)) {
            throw new BusinessException("Un symptôme avec ce nom " + name + " existe déjà.");
        }
    }

    public void checkNotUsed(Long symptomId) {
        if (symptomHealthRecordRepository.existsBySymptomId(symptomId)) {
            throw new BusinessException(
                    "Impossible de supprimer le symptôme car il est utilisé dans le carnet de santé."
            );
        }
    }
}
