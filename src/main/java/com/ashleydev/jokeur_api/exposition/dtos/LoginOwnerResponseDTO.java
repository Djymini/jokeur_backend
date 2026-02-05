package com.ashleydev.jokeur_api.exposition.dtos;

import com.ashleydev.jokeur_api.persistence.entities.OwnerEntity;

public record LoginOwnerResponseDTO(
        String token,
        String email,
        String role
) {

    public static LoginOwnerResponseDTO fromEntity(String token, OwnerEntity user) {
        return new LoginOwnerResponseDTO(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }
}