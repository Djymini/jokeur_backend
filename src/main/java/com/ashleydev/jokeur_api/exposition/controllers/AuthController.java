package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.exposition.dtos.LoginOwnerRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.LoginOwnerResponseDTO;
import com.ashleydev.jokeur_api.exposition.dtos.RegisterOwnerRequestDTO;
import com.ashleydev.jokeur_api.persistence.entities.OwnerEntity;
import com.ashleydev.jokeur_api.persistence.repositories.OwnerRepository;
import com.ashleydev.jokeur_api.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    private OwnerRepository ownerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> registerOwner(@RequestBody RegisterOwnerRequestDTO request) {
        boolean alreadyExists = ownerRepository.existsByEmail(request.email());
        if (alreadyExists) {
            String response = "Cet email est déjà utilisé !";
            return ResponseEntity.badRequest().body(response);
        }

        OwnerEntity user = request.toEntity();
        // 👇 On SET le mot de passe depuis le Controller, pas depuis le Mapper
        user.setPassword(passwordEncoder.encode(request.password()));
       ownerRepository.save(user);

        String response = "Utilisateur inscrit avec succès !";
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginOwnerResponseDTO> authenticateUser(@RequestBody LoginOwnerRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        OwnerEntity authenticatedUser = (OwnerEntity) authentication.getPrincipal();
        String token = jwtUtil.generateToken(authenticatedUser);

        LoginOwnerResponseDTO response = LoginOwnerResponseDTO.fromEntity(token, authenticatedUser);
        return ResponseEntity.ok(response);
    }

}
