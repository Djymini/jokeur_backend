package com.ashleydev.jokeur_api.integration;

import com.ashleydev.jokeur_api.domain.enums.pets.AnimalType;
import com.ashleydev.jokeur_api.domain.enums.pets.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.pets.PetColor;
import com.ashleydev.jokeur_api.domain.enums.pets.PetSex;
import com.ashleydev.jokeur_api.domain.services.SymptomHealthRecordService;
import com.ashleydev.jokeur_api.exposition.dtos.symptomhealthrecord.AddSymptomToHealthRecordRequestDTO;
import com.ashleydev.jokeur_api.persistence.entities.HealthRecordEntity;
import com.ashleydev.jokeur_api.persistence.entities.Role;
import com.ashleydev.jokeur_api.persistence.entities.SymptomEntity;
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomHealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.SymptomRepository;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.profiles.active=integration")
@Transactional
class SymptomHealthRecordServiceTest {

    @Autowired
    private SymptomHealthRecordService service;

    @Autowired
    private SymptomRepository symptomRepository;

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Autowired
    private SymptomHealthRecordRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void addSymptom_shouldSaveAssociation() {

        SymptomEntity symptom = new SymptomEntity();
        symptom.setName("Toux");
        symptomRepository.save(symptom);
        UserEntity user = new UserEntity();
        user.setName("Jean");
        user.setFirstname("Moulin");
        user.setEmail("test@test.fr");
        user.setPassword("123456");
        user.setRole(Role.OWNER);

        userRepository.save(user);

        HealthRecordEntity healthRecord = new HealthRecordEntity();
        healthRecord.setPetName("Felix");
        healthRecord.setAnimalType(AnimalType.CAT);
        healthRecord.setBreed(PetBreed.PERSIAN);
        healthRecord.setSex(PetSex.MALE);
        healthRecord.setBirthDate(LocalDate.of(2022, 1, 1));
        healthRecord.setCurrentWeight(healthRecord.getCurrentWeight());
        healthRecord.setColor(PetColor.BLACK);
        healthRecord.setIdentificationNumber("ID123");
        healthRecord.setTattoo("T123");
        healthRecord.setAllergy("none");
        healthRecord.setUser(user);

        healthRecordRepository.save(healthRecord);

        AddSymptomToHealthRecordRequestDTO dto =
                new AddSymptomToHealthRecordRequestDTO(
                        healthRecord.getId(),
                        symptom.getId(),
                        LocalDate.now(),
                        "toux légère"
                );

        service.addSymptomToHealthRecord(dto);

        boolean exists =
                repository.existsByHealthRecordIdAndSymptomId(
                        healthRecord.getId(),
                        symptom.getId()
                );

        assertTrue(exists);
    }
}