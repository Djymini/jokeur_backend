package com.ashleydev.jokeur_api.persistence.repositories;

import com.ashleydev.jokeur_api.persistence.entities.AppointmentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppoinmentRepository extends JpaRepository<AppointmentEntity, Long> {
  @Query(
    """
        Select app from AppointmentEntity app
        where app.healthRecord.user.id = :userId
        and app.dateTime >= CURRENT_TIMESTAMP
        order by app.dateTime asc
    """
  )
  List<AppointmentEntity> getAllAppointement(@Param("userId") Long userId);
}
