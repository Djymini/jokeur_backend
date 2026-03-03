package com.ashleydev.jokeur_api.persistence.repositories;

import com.ashleydev.jokeur_api.persistence.entities.ReminderEntity;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReminderRepository extends JpaRepository<ReminderEntity, Long> {
  @Query(
    """
    SELECT r FROM ReminderEntity r
    WHERE r.user.id = :userId
    and r.status = "PENDING"
    and r.reminderDate between CURRENT_DATE and :maxIntervalDate
    order by r.reminderDate asc
    """
  )
  Page<ReminderEntity> findPendingReminder(@Param("userId") Long userId, @Param("maxIntervalDate") LocalDate maxIntervalDate, Pageable pageable);
}
