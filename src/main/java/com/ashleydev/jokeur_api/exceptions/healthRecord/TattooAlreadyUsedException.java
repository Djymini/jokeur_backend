package com.ashleydev.jokeur_api.exceptions.healthRecord;

public class TattooAlreadyUsedException extends RuntimeException {
    public TattooAlreadyUsedException(String tattoo) {
        super("Tattoo number '" + tattoo + "' is already in use.");
    }
}