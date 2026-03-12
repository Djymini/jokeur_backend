/* package com.ashleydev.jokeur_api.persistence.repositories;

import com.ashleydev.jokeur_api.persistence.entities.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

    Optional<PasswordResetTokenEntity> findTopByUserIdAndUsedAtIsNullAndExpiresAtAfterOrderByCreatedAtDesc(
            Long userId, LocalDateTime now
    );

    Optional<PasswordResetTokenEntity> findByTokenHash(String tokenHash);

    void deleteByExpiresAtBefore(LocalDateTime now);
}
*/
