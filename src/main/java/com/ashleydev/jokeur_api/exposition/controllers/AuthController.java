package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.AuthService;
import com.ashleydev.jokeur_api.exposition.dtos.*;
import com.ashleydev.jokeur_api.exposition.dtos.vaccine.ResetPasswordRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<RegisterUserResponseDTO> registerUser(@RequestBody RegisterUserRequestDTO dto) {
    return ResponseEntity.ok(authService.register(dto));
  }

  @PostMapping("/login")
  public ResponseEntity<LoginUserResponseDTO> authenticateUser(@RequestBody LoginUserRequestDTO request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<Void> forgotPasswordUser(@RequestBody ForgotPasswordUserRequestDTO dto) {
    authService.forgotPassword(dto);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequestDTO dto) {
    authService.resetPassword(dto);
    return ResponseEntity.noContent().build();
  }
}
