package com.ashleydev.jokeur_api.persistence.repositories;

import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByEmail(String email);
  boolean existsByEmail(String email);
  boolean existsByPseudo(String pseudo);
  Optional<UserEntity> findByResetTokenHash(String resetTokenHash);
}
