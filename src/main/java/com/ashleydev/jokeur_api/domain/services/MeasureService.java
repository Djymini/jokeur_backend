package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.domain.enums.MeasureType;
import com.ashleydev.jokeur_api.domain.rules.MeasureRules;
import com.ashleydev.jokeur_api.exceptions.measure.MeasureNotFoundException;
import com.ashleydev.jokeur_api.exposition.dtos.measure.MeasureRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.measure.MeasureResponseDto;
import com.ashleydev.jokeur_api.exposition.mappers.MeasureMapper;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.MeasureEntity;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.measure.MeasureRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeasureService {

  @Autowired
  private MeasureRepository measureRepository;

  @Autowired
  private HealthRecordRepository healthRecordRepository;

  public MeasureResponseDto create(MeasureRequestDto request) {
    MeasureRules.validateBeforeCreation(request.measureType());
    HealthRecordEntity healthRecord = healthRecordRepository.findByHealthRecordNumber(request.healthRecordNumber()).get();
    MeasureEntity newEntity = MeasureMapper.toEntity(request, healthRecord);

    MeasureEntity response = measureRepository.save(newEntity);
    return MeasureMapper.toDto(response);
  }

  public List<MeasureResponseDto> getByType(String type, Long healthRecordNumber) {
    MeasureRules.validateType(type);
    List<MeasureEntity> measureList = measureRepository.findAllByHealthRecordNumber(healthRecordNumber, MeasureType.valueOf(type));
    List<MeasureResponseDto> response = new ArrayList<MeasureResponseDto>();
    for (MeasureEntity measure : measureList) {
      response.add(MeasureMapper.toDto(measure));
    }

    return response;
  }

  public MeasureResponseDto update(Long healthRecordNumber, Long measureId, int newValue) {
    checkHealthRecordNumberAndId(healthRecordNumber, measureId);
    MeasureEntity measureForUpdate = measureRepository.findById(measureId).get();
    measureForUpdate.setValue(newValue);
    MeasureEntity measureUpdated = measureRepository.save(measureForUpdate);
    return MeasureMapper.toDto(measureUpdated);
  }

  public void delete(Long healthRecordNumber, Long measureId) {
    checkHealthRecordNumberAndId(healthRecordNumber, measureId);
    measureRepository.deleteById(measureId);
  }

  private void checkHealthRecordNumberAndId(Long healthRecordNumber, Long measureId) {
    if (!measureRepository.existByHealthRecordNumberAndId(healthRecordNumber, measureId)) {
      throw new MeasureNotFoundException("The measure : " + measureId + " of Health record number : " + healthRecordNumber + "doesn't exist");
    }
  }
}
