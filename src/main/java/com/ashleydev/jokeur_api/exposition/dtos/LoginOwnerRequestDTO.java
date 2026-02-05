package com.ashleydev.jokeur_api.exposition.dtos;

public record LoginOwnerRequestDTO(
        String email,
        String password
) {}