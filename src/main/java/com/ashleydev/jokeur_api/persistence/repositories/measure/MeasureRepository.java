package com.ashleydev.jokeur_api.persistence.repositories.measure;

import com.ashleydev.jokeur_api.domain.enums.MeasureType;
import com.ashleydev.jokeur_api.persistence.entities.MeasureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasureRepository extends JpaRepository<MeasureEntity, Long> {
    List<MeasureEntity> findByHealthRecordNumberAndType(Long healthRecordNumber, MeasureType type);

    Optional<MeasureEntity> findById(Long id);

    void deleteById(Long id);
}
