package com.ashleydev.jokeur_api.exposition.dtos;

public record UserMeResponseDTO(
        String email,
        String pseudo,
        String role
) {}
