package com.ashleydev.jokeur_api.exposition.mappers;

import com.ashleydev.jokeur_api.exposition.dtos.owner.OwnerResponseDTO;
import com.ashleydev.jokeur_api.persistence.entities.OwnerEntity;

public class OwnerMapper {

  private OwnerMapper() {}

  public static OwnerResponseDTO toDto(OwnerEntity entity) {
    OwnerResponseDTO dto = new OwnerResponseDTO();
    dto.setIdOwner(entity.getIdOwner());
    dto.setEmail(entity.getEmail());
    dto.setName(entity.getName());
    dto.setPhoneNumber(entity.getPhoneNumber());
    return dto;
  }
}
