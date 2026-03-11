package com.ashleydev.jokeur_api.persistence.repositories;

import com.ashleydev.jokeur_api.persistence.entities.AppointmentEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppoinmentRepository extends JpaRepository<AppointmentEntity, Long> {
  @Query(
    """
        Select app from AppointmentEntity app
        where app.user.id  = :userId
        and app.dateTime >= CURRENT_TIMESTAMP
        order by app.dateTime asc
    """
  )
  Page<AppointmentEntity> getAllAppointement(@Param("userId") Long userId, Pageable pageable);

  @Query(
    """
    Select app from AppointmentEntity app
    WHERE app.user.id = :userId
    and app.dateTime between :startDate and :endDate
    order by app.dateTime asc
    """
  )
  List<AppointmentEntity> findPendingAppointmentDuringPeriod(
    @Param("userId") Long userId,
    @Param("startDate") LocalDate startDate,
    @Param("endDate") LocalDate endDate
  );

  @Query(
    """
    SELECT app FROM AppointmentEntity app
    WHERE app.user.id = :userId
    """
  )
  List<AppointmentEntity> findAllByUserId(@Param("userId") Long userId);
}
