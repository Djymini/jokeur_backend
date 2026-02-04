package com.ashleydev.jokeur_api.persistence.repositories.healthRecord;

import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecordEntity, Long> {
  List<HealthRecordDashboardView> findByOwner_IdOwner(Long ownerId);

  List<HealthRecordMyAnimalsView> findAllByOwner_IdOwner(Long ownerId);
}
