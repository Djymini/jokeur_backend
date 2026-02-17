package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.exceptions.owner.OwnerEmailAlreadyUsedException;
import com.ashleydev.jokeur_api.exceptions.owner.OwnerNotFoundException;
import com.ashleydev.jokeur_api.exceptions.owner.OwnerUpdateEmptyException;
import com.ashleydev.jokeur_api.exposition.dtos.owner.OwnerCreateDTO;
import com.ashleydev.jokeur_api.exposition.dtos.owner.OwnerResponseDTO;
import com.ashleydev.jokeur_api.exposition.dtos.owner.OwnerUpdateDTO;
import com.ashleydev.jokeur_api.mappers.OwnerMapper;
import com.ashleydev.jokeur_api.persistence.entities.OwnerEntity;
import com.ashleydev.jokeur_api.persistence.repositories.owner.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
@Transactional
public class OwnerService {

  private final OwnerRepository ownerRepository;

  @Autowired
  public OwnerService(OwnerRepository ownerRepository) {
    this.ownerRepository = ownerRepository;
  }

  public OwnerResponseDTO create(OwnerCreateDTO dto) {
    if (ownerRepository.findByEmail(dto.getEmail()).isPresent()) {
      throw new OwnerEmailAlreadyUsedException(dto.getEmail());
    }

    OwnerEntity entity = new OwnerEntity();
    entity.setEmail(dto.getEmail());
    entity.setName(dto.getName());
    entity.setPhoneNumber(dto.getPhoneNumber());

    OwnerEntity saved = ownerRepository.save(entity);
    return OwnerMapper.toDto(saved);
  }

  @Transactional(readOnly = true)
  public OwnerResponseDTO getById(Long ownerId) {
    OwnerEntity entity = ownerRepository.findById(ownerId).orElseThrow(() -> new OwnerNotFoundException(ownerId));

    return OwnerMapper.toDto(entity);
  }

  @Transactional(readOnly = true)
  public OwnerResponseDTO getByEmail(String email) {
    OwnerEntity entity = ownerRepository.findByEmail(email).orElseThrow(() -> new OwnerNotFoundException(email));

    return OwnerMapper.toDto(entity);
  }

  public OwnerResponseDTO updatePartial(Long ownerId, OwnerUpdateDTO dto) {
    if (dto == null || !dto.hasAtLeastOneField()) {
      throw new OwnerUpdateEmptyException();
    }

    OwnerEntity entity = ownerRepository.findById(ownerId).orElseThrow(() -> new OwnerNotFoundException(ownerId));

    if (dto.getName() != null) {
      entity.setName(dto.getName());
    }
    if (dto.getPhoneNumber() != null) {
      entity.setPhoneNumber(dto.getPhoneNumber());
    }

    OwnerEntity saved = ownerRepository.save(entity);
    return OwnerMapper.toDto(saved);
  }

  public void delete(Long ownerId) {
    if (!ownerRepository.existsById(ownerId)) {
      throw new OwnerNotFoundException(ownerId);
    }

    try {
      ownerRepository.deleteById(ownerId);
    } catch (DataIntegrityViolationException ex) {
      throw new OwnerDeletionForbiddenException();
    }
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  private static class OwnerDeletionForbiddenException extends RuntimeException {

    private OwnerDeletionForbiddenException() {
      super("Owner cannot be deleted because dependent data exists.");
    }
  }
}
