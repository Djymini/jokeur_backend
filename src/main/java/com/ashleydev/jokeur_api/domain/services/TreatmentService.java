package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.annotations.ValidateHealthRecord;
import com.ashleydev.jokeur_api.annotations.ValidateTreatment;
import com.ashleydev.jokeur_api.domain.enums.ReminderType;
import com.ashleydev.jokeur_api.domain.rules.TreatmentRules;
import com.ashleydev.jokeur_api.exceptions.reminder.ReminderNotFoundException;
import com.ashleydev.jokeur_api.exceptions.treatment.TreatmentDeleteFailedException;
import com.ashleydev.jokeur_api.exceptions.treatment.TreatmentNotFoundException;
import com.ashleydev.jokeur_api.exposition.dtos.treatment.TreatmentDetailRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.treatment.TreatmentRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.treatment.TreatmentResponseDto;
import com.ashleydev.jokeur_api.mappers.TreatmentMapper;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.ReminderEntity;
import com.ashleydev.jokeur_api.persistence.entities.TreatmentEntity;
import com.ashleydev.jokeur_api.persistence.repositories.ReminderRepository;
import com.ashleydev.jokeur_api.persistence.repositories.TreatmentRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TreatmentService {

  @Autowired
  private TreatmentRepository treatmentRepository;

  @Autowired
  private ReminderRepository reminderRepository;

  @Autowired
  private HealthRecordRepository healthRecordRepository;

  @ValidateHealthRecord
  public List<TreatmentResponseDto> getAllByHealthRecordId(Long healthRecordId) {
    return treatmentRepository.findAllByHealthRecordI(healthRecordId).stream().map(TreatmentMapper::toDto).toList();
  }

  public TreatmentResponseDto getById(Long id) {
    if (!treatmentRepository.existsById(id)) throw new TreatmentNotFoundException(id);
    TreatmentEntity treatmentEntity = treatmentRepository.findById(id).get();

    return TreatmentMapper.toDto(treatmentEntity);
  }

  @ValidateHealthRecord
  public TreatmentResponseDto create(TreatmentRequestDto request) {
    HealthRecordEntity healthRecord = healthRecordRepository.findById(request.healthRecordId()).get();
    ReminderEntity newReminder = new ReminderEntity();
    newReminder.setType(ReminderType.TREATMENT);
    newReminder.setDescription(TreatmentRules.formatReminderTreatmentDescription(request.name(), healthRecord.getPetName()));
    newReminder.setReminderDate(request.treatmentReminderDate());
    newReminder.setUser(healthRecord.getUser());
    TreatmentEntity newTreatment = treatmentRepository.save(TreatmentMapper.toEntity(request, healthRecord, newReminder));
    return TreatmentMapper.toDto(newTreatment);
  }

  @ValidateHealthRecord
  @ValidateTreatment
  public TreatmentResponseDto update(TreatmentDetailRequestDto request) {
    if (!reminderRepository.existsById(request.reminder().id())) throw new ReminderNotFoundException(
      "Le rappel " + request.reminder().id() + " n'existe pas"
    );

    HealthRecordEntity healthRecord = healthRecordRepository.findById(request.healthRecordId()).get();

    ReminderEntity newReminder = reminderRepository.findById(request.reminder().id()).get();
    newReminder.setReminderDate(request.reminder().reminderDate());
    newReminder.setDescription(TreatmentRules.formatReminderTreatmentDescription(request.name(), healthRecord.getPetName()));

    TreatmentEntity response = treatmentRepository.save(TreatmentMapper.toEntity(request, healthRecord, newReminder));

    return TreatmentMapper.toDto(response);
  }

  @ValidateHealthRecord
  @ValidateTreatment
  public String delete(Long id, Long healthRecordId) {
    treatmentRepository.deleteById(id);

    if (treatmentRepository.existsById(id)) {
      throw new TreatmentDeleteFailedException("Treatment : " + id + " is not deleted");
    }

    return "Treatment : " + id + " is deleted";
  }
}
