package com.ashleydev.jokeur_api.persistence.repositories;

import com.ashleydev.jokeur_api.persistence.entities.ReminderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<ReminderEntity, Long> {

    // 2. Recherche par propriétaire
    @Query("""
    SELECT r FROM ReminderEntity r
    WHERE r.healthRecord.owner.id = :idOwner
    and r.reminderDate between CURRENT_DATE and :maxIntervalDate
    order by r.reminderDate asc
    """)
    List<ReminderEntity> findByOwnerId
    (@Param("idOwner") Long idOwner, @Param("maxIntervalDate") LocalDate maxIntervalDate);

}
