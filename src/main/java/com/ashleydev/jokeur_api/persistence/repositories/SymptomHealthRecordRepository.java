package com.ashleydev.jokeur_api.persistence.repositories;

import com.ashleydev.jokeur_api.persistence.entities.SymptomHealthRecordEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SymptomHealthRecordRepository extends JpaRepository<SymptomHealthRecordEntity, Long> {
  boolean existsByHealthRecordIdAndSymptomIdAndObservationDate(Long healthRecordId, Long symptomId, LocalDate observationDate);
  boolean existsBySymptomId(Long symptomId);
  List<SymptomHealthRecordEntity> findByHealthRecordId(Long healthRecordId);
}
