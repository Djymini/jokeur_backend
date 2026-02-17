package com.ashleydev.jokeur_api.persistence.repositories.healthRecord;

import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordDashboardDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordMyAnimalsDTO;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecordEntity, Long> {
  // ---- Dashboard

  @Query(
    """
      select new com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordDashboardDTO(
        hr.id,
        hr.petName
      )
      from HealthRecordEntity hr
      where hr.owner.id = :ownerId
      order by hr.petName asc
    """
  )
  List<HealthRecordDashboardDTO> findDashboardDtosByOwnerId(@Param("ownerId") Long ownerId);

  // ---- My Animals

  @Query(
    """
      select new com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordMyAnimalsDTO(
        hr.id,
        hr.petName,
        hr.animalType,
        hr.breed,
        hr.sex,
        hr.currentWeight
      )
      from HealthRecordEntity hr
      where hr.owner.id = :ownerId
      order by hr.petName asc
    """
  )
  List<HealthRecordMyAnimalsDTO> findMyAnimalsDtosByOwnerId(@Param("ownerId") Long ownerId);

  Optional<HealthRecordEntity> findById(Long id);

  boolean existsById(Long id);

  void deleteById(Long healthRecordNumber);

  @Query(
    """
    Select hr from HealthRecordEntity hr
    where hr.owner.id = :idOwner
    """
  )
  List<HealthRecordEntity> findAllAnimals(@Param("idOwner") Long idOwner);
}
