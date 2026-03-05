package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.SenderMailService;
import com.ashleydev.jokeur_api.exposition.dtos.*;
import com.ashleydev.jokeur_api.exposition.dtos.vaccine.ResetPasswordRequestDTO;
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import com.ashleydev.jokeur_api.security.JwtUtil;
import com.ashleydev.jokeur_api.security.ResetTokenUtils;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private SenderMailService senderMailService;

  @PostMapping("/register")
  public ResponseEntity<RegisterUserResponseDTO> registerUser(@RequestBody RegisterUserRequestDTO dto) {
    if (userRepository.existsByEmail(dto.email())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new RegisterUserResponseDTO("Cet email est déjà utilisé"));
    }

    if (userRepository.existsByPseudo(dto.username())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new RegisterUserResponseDTO("Ce pseudo est déjà utilisé."));
    }

    UserEntity user = dto.toEntity();
    user.setPassword(passwordEncoder.encode(dto.password()));
    userRepository.save(user);

    return ResponseEntity.ok(new RegisterUserResponseDTO("Utilisateur inscrit avec succès."));
  }

  @PostMapping("/login")
  public ResponseEntity<LoginUserResponseDTO> authenticateUser(@RequestBody LoginUserRequestDTO request) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

    UserEntity authenticateUser = (UserEntity) authentication.getPrincipal();
    String token = jwtUtil.generateToken(authenticateUser);

    LoginUserResponseDTO response = LoginUserResponseDTO.fromEntity(token, authenticateUser);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<Void> forgotPasswordUser(@RequestBody ForgotPasswordUserRequestDTO dto) {
    userRepository
      .findByEmail(dto.email())
      .ifPresent(user -> {
        String rawToken = ResetTokenUtils.generateToken(); // token en clair pour l'email
        String hash = ResetTokenUtils.sha256Hex(rawToken); // hash stocké en DB

        user.setResetTokenHash(hash);
        user.setResetTokenExpiresAt(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        senderMailService.sendResetPasswordEmail(user.getUsername(), rawToken);
      });

    return ResponseEntity.noContent().build();
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequestDTO dto) {
    String tokenHash = ResetTokenUtils.sha256Hex(dto.token());

    UserEntity user = userRepository
      .findByResetTokenHash(tokenHash)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token"));

    if (user.getResetTokenExpiresAt() == null || user.getResetTokenExpiresAt().isBefore(LocalDateTime.now())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired");
    }

    // update password (hash bcrypt)
    user.setPassword(passwordEncoder.encode(dto.newPassword()));

    // invalider le token (usage unique)
    user.setResetTokenHash(null);
    user.setResetTokenExpiresAt(null);

    userRepository.save(user);

    return ResponseEntity.noContent().build();
  }
}
