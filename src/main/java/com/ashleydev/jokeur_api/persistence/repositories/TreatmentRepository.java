package com.ashleydev.jokeur_api.persistence.repositories;

import com.ashleydev.jokeur_api.persistence.entities.TreatmentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TreatmentRepository extends JpaRepository<TreatmentEntity, Long> {
  @Query(
    """
    SELECT treatment FROM TreatmentEntity treatment
    WHERE treatment.healthRecordEntity.id = :healthRecordId
    """
  )
  List<TreatmentEntity> findAllByHealthRecordI(@Param("healthRecordId") Long healthRecordEntityId);
}
