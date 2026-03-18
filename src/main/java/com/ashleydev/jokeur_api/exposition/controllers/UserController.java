package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.UserService;
import com.ashleydev.jokeur_api.exposition.dtos.UserMeResponseDTO;
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/me")
  public ResponseEntity<UserMeResponseDTO> getCurrentUser(@AuthenticationPrincipal UserEntity user) {
    return ResponseEntity.ok(userService.getMe(user));
  }

  @PatchMapping("/me")
  public ResponseEntity<Void> updateProfile(@AuthenticationPrincipal UserEntity user, @RequestBody UpdateUserProfileRequestDTO dto) {
    userService.updateProfile(user, dto);
    return ResponseEntity.noContent().build();
  }

  public record UpdateUserProfileRequestDTO(String name, String firstname, String address) {}
}
