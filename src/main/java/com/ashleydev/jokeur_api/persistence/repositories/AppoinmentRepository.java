package com.ashleydev.jokeur_api.persistence.repositories;

import com.ashleydev.jokeur_api.persistence.entities.AppointmentEntity;
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
        where app.healthRecord.owner.id = :idOwner
        and app.dateTime >= CURRENT_TIMESTAMP
        order by app.dateTime asc
    """
  )
  Page<AppointmentEntity> getAllAppointement(@Param("idOwner") Long idOwner, Pageable pageable);
}
