package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.exposition.dtos.HealthRecordResponseDto;
import com.ashleydev.jokeur_api.mappers.HealthRecordMapper;
import com.ashleydev.jokeur_api.persistence.repositories.HealthRecordRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HealthRecordService {

    private final HealthRecordRepository healthRecordRepository;

    public List<HealthRecordResponseDto> getAllAnimals(Long idOwner){
        return healthRecordRepository.findAllAnimals(idOwner).stream().map(
                HealthRecordMapper::toDto
        ).toList();
    }
}
