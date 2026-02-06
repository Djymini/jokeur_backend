package com.ashleydev.jokeur_api.persistence.repositories.owner;

import com.ashleydev.jokeur_api.persistence.entities.OwnerEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<OwnerEntity, Long> {
  Optional<OwnerEntity> findByEmail(String email);
}
