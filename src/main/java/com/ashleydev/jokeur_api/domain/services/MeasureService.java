package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.domain.enums.MeasureType;
import com.ashleydev.jokeur_api.domain.rules.MeasureRules;
import com.ashleydev.jokeur_api.exceptions.healthRecord.HealthRecordNotFoundException;
import com.ashleydev.jokeur_api.exceptions.measure.MeasureDeleteFailedException;
import com.ashleydev.jokeur_api.exceptions.measure.MeasureNotFoundException;
import com.ashleydev.jokeur_api.exceptions.measure.MeasureUpdateNotChangeValueException;
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
    MeasureRules.validateType(request.measureType());

    if (!healthRecordRepository.existsById(request.healthRecordId())) {
      throw new HealthRecordNotFoundException(request.healthRecordId());
    }

    HealthRecordEntity healthRecord = healthRecordRepository.findById(request.healthRecordId()).get();
    MeasureEntity newEntity = MeasureMapper.toEntity(request, healthRecord);

    MeasureEntity response = measureRepository.save(newEntity);
    return MeasureMapper.toDto(response);
  }

  public List<MeasureResponseDto> getByType(String type, Long healthRecordId) {
    MeasureRules.validateType(type);
    List<MeasureEntity> measureList = measureRepository.findAllByHealthRecordIdAndType(
      healthRecordId,
      MeasureType.valueOf(type.toUpperCase())
    );
    List<MeasureResponseDto> response = new ArrayList<MeasureResponseDto>();
    for (MeasureEntity measure : measureList) {
      response.add(MeasureMapper.toDto(measure));
    }

    return response;
  }

  public MeasureResponseDto update(Long healthRecordId, Long measureId, float newValue) {
    checkHealthRecordIdAndId(healthRecordId, measureId);
    measureRepository.setMeasureById(healthRecordId, measureId, newValue);

    MeasureEntity checkChange = measureRepository.findById(measureId).get();
    if (checkChange.getMeasureValue() != newValue) {
      throw new MeasureUpdateNotChangeValueException("Measure : " + measureId + " not changed after update");
    }

    return MeasureMapper.toDto(checkChange);
  }

  public String delete(Long healthRecordId, Long measureId) {
    checkHealthRecordIdAndId(healthRecordId, measureId);
    measureRepository.deleteById(measureId);

    if (measureRepository.existByHealthRecordIdAndId(healthRecordId, measureId)) {
      throw new MeasureDeleteFailedException("Measure : " + measureId + " is not deleted");
    }

    return "Measure : " + measureId + " is deleted";
  }

  private void checkHealthRecordIdAndId(Long healthRecordId, Long measureId) {
    if (!measureRepository.existByHealthRecordIdAndId(healthRecordId, measureId)) {
      throw new MeasureNotFoundException("The measure : " + measureId + " of Health record number : " + healthRecordId + " doesn't exist");
    }
  }
}
