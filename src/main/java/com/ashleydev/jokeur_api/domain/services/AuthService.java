package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.exposition.dtos.*;
import com.ashleydev.jokeur_api.exposition.dtos.vaccine.ResetPasswordRequestDTO;
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import com.ashleydev.jokeur_api.security.JwtUtil;
import com.ashleydev.jokeur_api.security.ResetTokenUtils;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

  private final AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SenderMailService senderMailService;

  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public AuthService(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  public RegisterUserResponseDTO register(RegisterUserRequestDTO dto) {
    if (userRepository.existsByEmail(dto.email())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Cet email est déjà utilisé");
    }
    if (userRepository.existsByPseudo(dto.username())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Ce pseudo est déjà utilisé.");
    }

    UserEntity user = dto.toEntity();
    user.setPassword(passwordEncoder.encode(dto.password()));
    userRepository.save(user);

    return new RegisterUserResponseDTO("Utilisateur inscrit avec succès.");
  }

  public LoginUserResponseDTO login(LoginUserRequestDTO request) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

    UserEntity authenticatedUser = (UserEntity) authentication.getPrincipal();
    String token = jwtUtil.generateToken(authenticatedUser);

    return LoginUserResponseDTO.fromEntity(token, authenticatedUser);
  }

  public void forgotPassword(ForgotPasswordUserRequestDTO dto) {
    userRepository
      .findByEmail(dto.email())
      .ifPresent(user -> {
        String rawToken = ResetTokenUtils.generateToken();
        String hash = ResetTokenUtils.sha256Hex(rawToken);

        user.setResetTokenHash(hash);
        user.setResetTokenExpiresAt(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        senderMailService.sendResetPasswordEmail(user.getUsername(), rawToken);
      });
  }

  public void resetPassword(ResetPasswordRequestDTO dto) {
    String tokenHash = ResetTokenUtils.sha256Hex(dto.token());

    UserEntity user = userRepository
      .findByResetTokenHash(tokenHash)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid token"));

    if (user.getResetTokenExpiresAt() == null || user.getResetTokenExpiresAt().isBefore(LocalDateTime.now())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired");
    }

    user.setPassword(passwordEncoder.encode(dto.newPassword()));
    user.setResetTokenHash(null);
    user.setResetTokenExpiresAt(null);
    userRepository.save(user);
  }
}
