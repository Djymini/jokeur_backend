package com.ashleydev.jokeur_api.persistence.repositories.vaccine;

import com.ashleydev.jokeur_api.persistence.entities.VaccineEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VaccineRepository extends JpaRepository<VaccineEntity, Long> {
  @Query(
    """
    SELECT vaccine FROM VaccineEntity vaccine
    WHERE vaccine.healthRecordEntity.id = :healthRecordId
    """
  )
  List<VaccineEntity> findAllByHealthRecordI(@Param("healthRecordId") Long healthRecordEntityId);
}
