package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.domain.enums.MeasureType;
import com.ashleydev.jokeur_api.exposition.dtos.measure.MeasureRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.measure.MeasureResponseDto;
import com.ashleydev.jokeur_api.exposition.mappers.MeasureMapper;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.MeasureEntity;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.measure.MeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeasureService {
    private final MeasureRepository measureRepository;
    private final HealthRecordRepository healthRecordRepository;

    @Autowired
    public MeasureService(MeasureRepository measureRepository, HealthRecordRepository healthRecordRepository) {
        this.measureRepository = measureRepository;
        this.healthRecordRepository = healthRecordRepository;
    }

    public MeasureResponseDto create(MeasureRequestDto request){
        HealthRecordEntity healthRecord = healthRecordRepository.findByHealthRecordNumber(request.healthRecordNumber()).get();
        MeasureEntity newEntity = MeasureMapper.toEntity(request, healthRecord);

        MeasureEntity response = measureRepository.save(newEntity);
        return MeasureMapper.toDto(response);
    }

    public List<MeasureResponseDto> getByType(String type, Long healthRecordNumber){
        List<MeasureEntity> measureList = measureRepository.findByHealthRecordNumberAndType(healthRecordNumber, MeasureType.valueOf(type));
        List<MeasureResponseDto> response= new ArrayList<MeasureResponseDto>();
        for(MeasureEntity measure : measureList){
            response.add(MeasureMapper.toDto(measure));
        }

        return response;
    }

    public MeasureResponseDto update(Long healthRecordNumber, Long measureId, int newValue){
        MeasureEntity measureForUpdate = measureRepository.findById(measureId).get();
        measureForUpdate.setValue(newValue);
        MeasureEntity measureUpdated = measureRepository.save(measureForUpdate);
        return MeasureMapper.toDto(measureUpdated);
    }

    public void delete(Long healthRecordNumber, Long measureId){
        measureRepository.deleteById(measureId);
    }
}
