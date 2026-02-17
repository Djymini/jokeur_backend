package com.ashleydev.jokeur_api.persistence.repositories.measure;

import com.ashleydev.jokeur_api.domain.enums.MeasureType;
import com.ashleydev.jokeur_api.persistence.entities.MeasureEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface MeasureRepository extends JpaRepository<MeasureEntity, Long> {
  @Query(
    """
    SELECT p FROM MeasureEntity p
    WHERE p.healthRecordEntity.id = :healthRecordEntityId"""
  )
  List<MeasureEntity> findByHealthRecordId(@Param("healthRecordEntityId") Long healthRecordEntityId);

  @Query(
    """
    SELECT p FROM MeasureEntity p
    WHERE p.healthRecordEntity.id = :healthRecordEntityId
    AND p.measureType = :measureType"""
  )
  List<MeasureEntity> findAllByHealthRecordIdAndType(
    @Param("healthRecordEntityId") Long healthRecordEntityId,
    @Param("measureType") MeasureType measureType
  );

  @Modifying(clearAutomatically = true)
  @Query("UPDATE MeasureEntity m SET m.measureValue = :newValue WHERE m.id = :id AND m.healthRecordEntity.id = :healthRecordId")
  void setMeasureById(@Param("healthRecordId") Long healthRecordId, @Param("id") Long id, @Param("newValue") float newValue);

  Optional<MeasureEntity> findById(Long id);
  void deleteById(Long id);

  @Query(
    """
        SELECT count(m) > 0
        FROM MeasureEntity m
        WHERE m.id = :id
        AND m.healthRecordEntity.id = :healthRecordId
    """
  )
  boolean existByHealthRecordIdAndId(@Param("healthRecordId") Long healthRecordId, @Param("id") Long id);
}
