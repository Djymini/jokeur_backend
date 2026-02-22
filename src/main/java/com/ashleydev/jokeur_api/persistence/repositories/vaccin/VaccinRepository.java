package com.ashleydev.jokeur_api.persistence.repositories.vaccin;

import com.ashleydev.jokeur_api.persistence.entities.VaccinEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VaccinRepository extends JpaRepository<VaccinEntity, Long> {
  @Query(
    """
    SELECT vaccin FROM VaccinEntity vaccin
    WHERE vaccin.healthRecordEntity.id = :healthRecordId
    """
  )
  List<VaccinEntity> findAllByHealthRecordI(@Param("healthRecordId") Long healthRecordEntityId);
}
