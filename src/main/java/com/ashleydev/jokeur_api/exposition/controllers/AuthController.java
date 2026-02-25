package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.exposition.dtos.LoginUserRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.LoginUserResponseDTO;
import com.ashleydev.jokeur_api.exposition.dtos.RegisterUserRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.RegisterUserResponseDTO;
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import com.ashleydev.jokeur_api.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  @PostMapping("/register")
  public ResponseEntity<RegisterUserResponseDTO> registerUser(@RequestBody RegisterUserRequestDTO dto) {

    if (userRepository.existsByEmail(dto.email())) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new RegisterUserResponseDTO("Cet email est déjà utilisé"));
    }

    if (userRepository.existsByPseudo(dto.username())) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new RegisterUserResponseDTO("Ce pseudo est déjà utilisé."));
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
}
