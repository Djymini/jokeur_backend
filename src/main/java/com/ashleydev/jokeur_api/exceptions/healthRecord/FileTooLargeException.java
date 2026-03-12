package com.ashleydev.jokeur_api.exceptions.healthRecord;

public class FileTooLargeException extends RuntimeException {
    public FileTooLargeException() {
        super("The file is too large (maximum 2MB).");
    }
}