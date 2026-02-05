package com.ashleydev.jokeur_api.persistence.repositories;
import com.ashleydev.jokeur_api.persistence.entities.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<OwnerEntity, Long> {
    Optional<OwnerEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}