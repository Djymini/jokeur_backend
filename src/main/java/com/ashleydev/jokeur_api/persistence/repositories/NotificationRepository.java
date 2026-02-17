package com.ashleydev.jokeur_api.persistence.repositories;

import com.ashleydev.jokeur_api.persistence.entities.NotificationEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
  Optional<NotificationEntity> findByLink(String link);

  @Query(
    """
    Select n from NotificationEntity n order by n.publishedAt desc
    """
  )
  List<NotificationEntity> getNotifications();
}
