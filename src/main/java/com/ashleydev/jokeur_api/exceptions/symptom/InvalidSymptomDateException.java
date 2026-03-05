package com.ashleydev.jokeur_api.exceptions.symptom;

public class InvalidSymptomDateException extends BusinessException{
    public InvalidSymptomDateException(){
        super("La date de fin ne peut pas être antérieure à la date de début");
    }
}
