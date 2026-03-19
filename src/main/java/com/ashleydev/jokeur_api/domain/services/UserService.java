package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.exposition.controllers.UserController;
import com.ashleydev.jokeur_api.exposition.dtos.UserMeResponseDTO;
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserMeResponseDTO getMe(UserEntity user) {
    return new UserMeResponseDTO(user.getEmail(), user.getRole().name(), user.getName(), user.getFirstname(), user.getAddress());
  }

  public void updateProfile(UserEntity user, UserController.UpdateUserProfileRequestDTO dto) {
    user.setName(dto.name().trim());
    user.setFirstname(dto.firstname().trim());

    String addr = dto.address();
    user.setAddress(addr == null || addr.isBlank() ? null : addr.trim());

    userRepository.save(user);
  }

  public void deleteAccount(UserEntity user) {
    userRepository.deleteById(user.getId());
  }
}
