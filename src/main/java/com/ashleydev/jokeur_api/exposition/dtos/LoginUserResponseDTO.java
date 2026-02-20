package com.ashleydev.jokeur_api.exposition.dtos;

import com.ashleydev.jokeur_api.persistence.entities.UserEntity;

public record LoginUserResponseDTO(String token, Long id, String firstName, String name, String email, String role) {
  public static LoginUserResponseDTO fromEntity(String token, UserEntity owner) {
    return new LoginUserResponseDTO(token, owner.getId(), owner.getFirstname(), owner.getName(), owner.getEmail(), owner.getRole().toString());
  }
}
