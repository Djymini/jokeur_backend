package com.ashleydev.jokeur_api.persistence.repositories;

import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthRecordRepository extends JpaRepository<HealthRecordEntity, Long> {

    @Query("""
           Select hr from HealthRecordEntity hr
           where hr.owner.id = :idOwner
           """
    )
    public List<HealthRecordEntity> findAllAnimals(@Param("idOwner") Long idOwner);
}
