package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.annotations.ValidateHealthRecord;
import com.ashleydev.jokeur_api.domain.enums.ReminderType;
import com.ashleydev.jokeur_api.exceptions.reminder.ReminderNotFoundException;
import com.ashleydev.jokeur_api.exceptions.vaccin.VaccinNotFoundException;
import com.ashleydev.jokeur_api.exposition.dtos.vaccin.VaccinRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.vaccin.VaccinResponseDto;
import com.ashleydev.jokeur_api.mappers.VaccinMapper;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.ReminderEntity;
import com.ashleydev.jokeur_api.persistence.entities.VaccinEntity;
import com.ashleydev.jokeur_api.persistence.repositories.ReminderRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.vaccin.VaccinRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VaccinService {

  @Autowired
  private VaccinRepository vaccinRepository;

  @Autowired
  private ReminderRepository reminderRepository;

  @Autowired
  private HealthRecordRepository healthRecordRepository;

  @ValidateHealthRecord
  public List<VaccinResponseDto> getAllByHealthRecordId(Long healthRecordId) {
    List<VaccinResponseDto> vaccinResponseList = new ArrayList<>();

    List<VaccinEntity> vaccinEntityList = vaccinRepository.findAllByHealthRecordI(healthRecordId);
    return vaccinRepository.findAllByHealthRecordI(healthRecordId).stream().map(VaccinMapper::toDto).toList();
  }

  public VaccinResponseDto getById(Long id) {
    if (!vaccinRepository.existsById(id)) throw new VaccinNotFoundException("Le vaccin n'existe pas");

    VaccinEntity vaccinEntity = vaccinRepository.findById(id).get();

    if (!reminderRepository.existsById(vaccinEntity.getReminderEntity().getId())) throw new ReminderNotFoundException(
      "Le vaccin ne possède pas de rappel"
    );

    return VaccinMapper.toDto(vaccinEntity);
  }

  @ValidateHealthRecord
  public VaccinResponseDto add(VaccinRequestDto request) {
    HealthRecordEntity healthRecord = healthRecordRepository.findById(request.healthRecordId()).get();

    ReminderEntity newReminder = new ReminderEntity();
    newReminder.setType(ReminderType.VACCINE);
    newReminder.setDescription(request.description());
    newReminder.setReminderDate(request.vaccinReminderDate());
    newReminder.setUser(healthRecord.getUser());
    ReminderEntity savedReminder = reminderRepository.save(newReminder);

    VaccinEntity newVaccin = vaccinRepository.save(VaccinMapper.toEntity(request, healthRecord, savedReminder));

    return VaccinMapper.toDto(newVaccin);
  }

  public void delete() {}
}
