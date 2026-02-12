package com.ashleydev.jokeur_api.persistence.repositories.measure;

import com.ashleydev.jokeur_api.domain.enums.MeasureType;
import com.ashleydev.jokeur_api.persistence.entities.MeasureEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasureRepository extends JpaRepository<MeasureEntity, Long> {
  @Query(
    """
    SELECT p FROM MeasureEntity p
    WHERE p.healthRecordEntity.healthRecordNumber = :healthRecordNumber
    AND p.measureType = :measureType"""
  )
  List<MeasureEntity> findAllByHealthRecordNumber(
    @Param("healthRecordNumber") Long healthRecordNumber,
    @Param("measureType") MeasureType measureType
  );

  @Modifying(clearAutomatically = true)
  @Query("UPDATE MeasureEntity m SET m.measureValue = :newValue WHERE m.id = :id AND m.healthRecordEntity.healthRecordNumber = :healthRecordNumber")
  void setMeasureById(@Param("healthRecordNumber") Long healthRecordNumber, @Param("id") Long id, @Param("newValue") float newValue);

  Optional<MeasureEntity> findById(Long id);
  void deleteById(Long id);

  @Query(
    """
        SELECT count(m) > 0
        FROM MeasureEntity m
        WHERE m.id = :id
        AND m.healthRecordEntity.healthRecordNumber = :healthRecordNumber
    """
  )
  boolean existByHealthRecordNumberAndId(@Param("healthRecordNumber") Long healthRecordNumber, @Param("id") Long id);
}
