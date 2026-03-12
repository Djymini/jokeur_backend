package com.ashleydev.jokeur_api.persistence.repositories;

import com.ashleydev.jokeur_api.persistence.entities.ReminderEntity;
import java.time.LocalDateTime;
import java.util.List;
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
  Page<ReminderEntity> findPendingReminder(@Param("userId") Long userId, @Param("maxIntervalDate") LocalDateTime maxIntervalDate, Pageable pageable);

  @Query(
    """
    SELECT r FROM ReminderEntity r
    WHERE r.user.id = :userId
    and r.status = "PENDING"
    and r.reminderDate between :startDate and :endDate
    order by r.reminderDate asc
    """
  )
  List<ReminderEntity> findPendingReminderDuringPeriod(
    @Param("userId") Long userId,
    @Param("startDate") LocalDateTime startDate,
    @Param("endDate") LocalDateTime endDate
  );
}
