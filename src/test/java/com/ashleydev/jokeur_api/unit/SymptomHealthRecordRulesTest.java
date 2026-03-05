package com.ashleydev.jokeur_api.unit;

import com.ashleydev.jokeur_api.domain.rules.SymptomHealthRecordRules;
import com.ashleydev.jokeur_api.exceptions.symptom.InvalidSymptomDateException;
import com.ashleydev.jokeur_api.exceptions.symptom.SymptomAlreadyAddedException;
import com.ashleydev.jokeur_api.exceptions.symptom.SymptomNotActiveException;
import com.ashleydev.jokeur_api.persistence.entities.SymptomEntity;
import com.ashleydev.jokeur_api.persistence.entities.SymptomHealthRecordEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SymptomHealthRecordRulesTest {
    @Test
    void should_throw_exception_if_endDate_before_startDate(){
        assertThrows(
                InvalidSymptomDateException.class,
                ()-> SymptomHealthRecordRules.checkDateConsistency(
                        LocalDate.of(2026,3,10),
                        LocalDate.of(2026, 3, 5)
                )
        );
    }

    @Test
    void should_throw_exception_if_symptom_not_active(){
        SymptomHealthRecordEntity symptomHealthRecord = new SymptomHealthRecordEntity();
        symptomHealthRecord.setId(1L);
        symptomHealthRecord.setIsActive(false);

        assertThrows(
                SymptomNotActiveException.class,
                ()-> SymptomHealthRecordRules.checkSymptomIsActive(symptomHealthRecord)
        );
    }


    @Test
    void should_throw_exception_when_symptom_already_added() {

        Long symptomId = 1L;
        Long healthRecordId = 10L;

        SymptomEntity symptom = new SymptomEntity();
        symptom.setId(1L);

        SymptomHealthRecordEntity shr = new SymptomHealthRecordEntity();
        shr.setSymptom(symptom);

        List<SymptomHealthRecordEntity> list = new ArrayList<>();
        list.add(shr);

        assertThrows(
                SymptomAlreadyAddedException.class,
                () -> SymptomHealthRecordRules.checkNotAlreadyAdded(
                        symptomId,
                        healthRecordId,
                        list
                )
        );
    }
    @Test
    void should_not_throw_exception_when_symptom_not_present() {

        Long symptomId = 2L;
        Long healthRecordId = 10L;

        SymptomEntity symptom = new SymptomEntity();
        symptom.setId(1L);

        SymptomHealthRecordEntity shr = new SymptomHealthRecordEntity();
        shr.setSymptom(symptom);

        List<SymptomHealthRecordEntity> list = new ArrayList<>();
        list.add(shr);

        assertDoesNotThrow(() ->
                SymptomHealthRecordRules.checkNotAlreadyAdded(
                        symptomId,
                        healthRecordId,
                        list
                )
        );
    }


}
