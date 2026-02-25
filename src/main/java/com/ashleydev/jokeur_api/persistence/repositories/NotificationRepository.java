package com.ashleydev.jokeur_api.persistence.repositories;

import com.ashleydev.jokeur_api.persistence.entities.NotificationEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
  Optional<NotificationEntity> findByLink(String link);

  Page<NotificationEntity> findAllByOrderByPublishedAtDesc(Pageable pageable);
}
