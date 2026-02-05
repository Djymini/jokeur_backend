package com.ashleydev.jokeur_api.persistence.repositories.healthRecord;

import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordResponseDTO;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecordEntity, Long> {

    List<HealthRecordDashboardView> findByOwner_IdOwner(Long ownerId);

    List<HealthRecordMyAnimalsView> findAllByOwner_IdOwner(Long ownerId);

    @Query("""
    select new com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordResponseDTO(
      hr.healthRecordNumber,
      hr.owner.idOwner,
      hr.petName,
      hr.animalType,
      hr.breed,
      hr.sex,
      hr.birthDate,
      hr.currentWeight,
      hr.color,
      hr.identificationNumber,
      hr.tattooNumber,
      hr.allergy
    )
    from HealthRecordEntity hr
    where hr.healthRecordNumber = :healthRecordNumber
  """)
    Optional<HealthRecordResponseDTO> findResponseById(@Param("healthRecordNumber") Long healthRecordNumber);
}
