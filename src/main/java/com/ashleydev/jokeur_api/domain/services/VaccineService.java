package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.annotations.ValidateHealthRecord;
import com.ashleydev.jokeur_api.annotations.ValidateVaccine;
import com.ashleydev.jokeur_api.domain.enums.ReminderType;
import com.ashleydev.jokeur_api.domain.rules.VaccineRules;
import com.ashleydev.jokeur_api.exceptions.reminder.ReminderNotFoundException;
import com.ashleydev.jokeur_api.exceptions.vaccin.VaccinNotFoundException;
import com.ashleydev.jokeur_api.exceptions.vaccin.VaccineDeleteFailedException;
import com.ashleydev.jokeur_api.exposition.dtos.vaccine.VaccineDetailRequestDto;
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

    return VaccinMapper.toDto(vaccineEntity);
  }

  @ValidateHealthRecord
  public VaccineResponseDto create(VaccineRequestDto request) {
    HealthRecordEntity healthRecord = healthRecordRepository.findById(request.healthRecordId()).get();
    ReminderEntity newReminder = new ReminderEntity();
    newReminder.setType(ReminderType.VACCINE);
    newReminder.setDescription(VaccineRules.formatReminderVaccineDescription(request.name()));
    newReminder.setReminderDate(request.vaccineReminderDate());
    newReminder.setUser(healthRecord.getUser());
    VaccineEntity newVaccin = vaccineRepository.save(VaccinMapper.toEntity(request, healthRecord, newReminder));
    return VaccinMapper.toDto(newVaccin);
  }

  @ValidateHealthRecord
  @ValidateVaccine
  public VaccineResponseDto update(VaccineDetailRequestDto request) {
    if (!reminderRepository.existsById(request.reminder().id())) throw new ReminderNotFoundException(
      "Le rappel " + request.reminder().id() + " n'existe pas"
    );

    ReminderEntity newReminder = reminderRepository.findById(request.reminder().id()).get();
    newReminder.setDescription(request.reminder().description());
    newReminder.setReminderDate(request.reminder().reminderDate());

    HealthRecordEntity healthRecord = healthRecordRepository.findById(request.healthRecordId()).get();

    VaccineEntity response = vaccineRepository.save(VaccinMapper.toEntity(request, healthRecord, newReminder));

    return VaccinMapper.toDto(response);
  }

  @ValidateVaccine
  @ValidateHealthRecord
  public String delete(Long id, Long healthRecordId) {
    vaccineRepository.deleteById(id);

    if (vaccineRepository.existsById(id)) {
      throw new VaccineDeleteFailedException("Vaccine : " + id + " is not deleted");
    }

    return "Vaccine : " + id + " is deleted";
  }
}
