package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/me")
  public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal UserEntity user) {
    Map<String, Object> userInfo = new HashMap<>();
    userInfo.put("id", user.getUsername());
    userInfo.put("role", user.getRole().name());
    userInfo.put("name", user.getName());
    userInfo.put("firstname", user.getFirstname());
    userInfo.put("address", user.getAddress());
    return ResponseEntity.ok(userInfo);
  }

  @PatchMapping("/me")
  public ResponseEntity<Void> updateProfile(@AuthenticationPrincipal UserEntity user, @RequestBody UpdateUserProfileRequestDTO dto) {
    user.setName(dto.name().trim());
    user.setFirstname(dto.firstname().trim());

    String addr = dto.address();
    user.setAddress(addr == null || addr.isBlank() ? null : addr.trim()); // si vide => null en bd

    userRepository.save(user);
    return ResponseEntity.noContent().build();
  }

  public record UpdateUserProfileRequestDTO(String name, String firstname, String address) {}
}
