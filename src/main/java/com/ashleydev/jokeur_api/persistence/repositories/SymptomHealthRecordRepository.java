package com.ashleydev.jokeur_api.persistence.repositories;

import com.ashleydev.jokeur_api.persistence.entities.SymptomHealthRecordEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymptomHealthRecordRepository extends JpaRepository<SymptomHealthRecordEntity, Long> {
  List<SymptomHealthRecordEntity> findByHealthRecordIdAndStartDateBetween(Long HealthRecordId, LocalDate start, LocalDate end);

  boolean existsBySymptomId(Long symptomId);
}
