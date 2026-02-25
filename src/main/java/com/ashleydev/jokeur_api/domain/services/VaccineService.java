package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.annotations.ValidateHealthRecord;
import com.ashleydev.jokeur_api.annotations.ValidateVaccine;
import com.ashleydev.jokeur_api.domain.enums.ReminderType;
import com.ashleydev.jokeur_api.exceptions.reminder.ReminderNotFoundException;
import com.ashleydev.jokeur_api.exceptions.vaccin.VaccinNotFoundException;
import com.ashleydev.jokeur_api.exposition.dtos.vaccine.VaccineRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.vaccine.VaccineResponseDto;
import com.ashleydev.jokeur_api.mappers.VaccinMapper;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.ReminderEntity;
import com.ashleydev.jokeur_api.persistence.entities.VaccineEntity;
import com.ashleydev.jokeur_api.persistence.repositories.ReminderRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.vaccine.VaccineRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VaccineService {

  @Autowired
  private VaccineRepository vaccineRepository;

  @Autowired
  private ReminderRepository reminderRepository;

  @Autowired
  private HealthRecordRepository healthRecordRepository;

  @ValidateHealthRecord
  public List<VaccineResponseDto> getAllByHealthRecordId(Long healthRecordId) {
    List<VaccineResponseDto> vaccinResponseList = new ArrayList<>();
    List<VaccineEntity> vaccineEntityList = vaccineRepository.findAllByHealthRecordI(healthRecordId);
    return vaccineRepository.findAllByHealthRecordI(healthRecordId).stream().map(VaccinMapper::toDto).toList();
  }

  public VaccineResponseDto getById(Long id) {
    if (!vaccineRepository.existsById(id)) throw new VaccinNotFoundException(id);
    VaccineEntity vaccineEntity = vaccineRepository.findById(id).get();
    if (!reminderRepository.existsById(vaccineEntity.getReminderEntity().getId())) throw new ReminderNotFoundException(
      "Le vaccin ne possède pas de rappel"
    );

    return VaccinMapper.toDto(vaccineEntity);
  }

  @ValidateHealthRecord
  public VaccineResponseDto add(VaccineRequestDto request) {
    HealthRecordEntity healthRecord = healthRecordRepository.findById(request.healthRecordId()).get();
    ReminderEntity newReminder = new ReminderEntity();
    newReminder.setType(ReminderType.VACCINE);
    newReminder.setDescription(request.description());
    newReminder.setReminderDate(request.vaccinReminderDate());
    newReminder.setUser(healthRecord.getUser());
    ReminderEntity savedReminder = reminderRepository.save(newReminder);
    VaccineEntity newVaccin = vaccineRepository.save(VaccinMapper.toEntity(request, healthRecord, savedReminder));
    return VaccinMapper.toDto(newVaccin);
  }

  @ValidateHealthRecord
  @ValidateVaccine
  public void delete(Long id, Long healthRecordId) {
    vaccineRepository.deleteById(id);
  }
}
