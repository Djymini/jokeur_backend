package com.ashleydev.jokeur_api.exceptions.symptom;

public class SymptomNotActiveException extends BusinessException{
    public SymptomNotActiveException(Long symptomId){
        super("Le symptôme " + symptomId + " est inactif");
    }
}
