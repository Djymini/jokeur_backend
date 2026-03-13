package com.ashleydev.jokeur_api.integration;

import com.ashleydev.jokeur_api.domain.enums.pets.AnimalType;
import com.ashleydev.jokeur_api.domain.enums.pets.PetBreed;
import com.ashleydev.jokeur_api.domain.enums.pets.PetColor;
import com.ashleydev.jokeur_api.domain.enums.pets.PetSex;
import com.ashleydev.jokeur_api.domain.services.AgendaService;
import com.ashleydev.jokeur_api.domain.services.AppointmentService;
import com.ashleydev.jokeur_api.domain.services.HealthRecordService;
import com.ashleydev.jokeur_api.domain.services.VaccineService;
import com.ashleydev.jokeur_api.exposition.dtos.agenda.AgendaResponseDto;
import com.ashleydev.jokeur_api.exposition.dtos.appointment.AppointmentRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordRequestDTO;
import com.ashleydev.jokeur_api.exposition.dtos.healthRecord.HealthRecordResponseDto;
import com.ashleydev.jokeur_api.exposition.dtos.vaccine.VaccineRequestDto;
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import com.ashleydev.jokeur_api.persistence.repositories.AppoinmentRepository;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import com.ashleydev.jokeur_api.persistence.repositories.healthRecord.HealthRecordRepository;
import com.ashleydev.jokeur_api.persistence.repositories.vaccine.VaccineRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.profiles.active=integration")
@ActiveProfiles("integration")
@Transactional
public class AgendaServiceIntegrationTest {
    @Autowired
    private AgendaService agendaService;

    @Autowired
    private VaccineService vaccineService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private HealthRecordService healthRecordService;

    @Autowired private AppoinmentRepository appoinmentRepository;
    @Autowired private VaccineRepository vaccineRepository;
    @Autowired private HealthRecordRepository healthRecordRepository;
    @Autowired private UserRepository userRepository;

    private Long userId;
    private HealthRecordResponseDto healthRecord;
    private Long duration = 30L;

    private AppointmentRequestDto appointment1;
    private AppointmentRequestDto appointment2;
    private AppointmentRequestDto appointment3;
    private AppointmentRequestDto appointment4;

    private VaccineRequestDto vaccine1;
    private VaccineRequestDto vaccine2;
    private VaccineRequestDto vaccine3;
    private VaccineRequestDto vaccine4;

    @BeforeEach
    void setUp() {
        healthRecordRepository.deleteAll();
        userRepository.deleteAll();
        vaccineRepository.deleteAll();

        UserEntity user = new UserEntity();
        user.setEmail("owner@test.com");
        user.setFirstname("Owner");
        user.setName("Test");
        user.setPhoneNumber("0600000000");
        user.setPassword("P@ssword1234");
        userId = userRepository.save(user).getId();

        HealthRecordRequestDTO healthRecordRequest = new HealthRecordRequestDTO(
                userId,
                "Naya",
                AnimalType.values()[0],
                PetBreed.LABRADOR,
                PetSex.values()[0],
                LocalDate.now().minusYears(2),
                new BigDecimal("4.20"),
                PetColor.BLACK,
                "CHIP12312",
                null,
                null
        );

        healthRecord = healthRecordService.create(healthRecordRequest);

        vaccine1 = new VaccineRequestDto("Naccie", "Vaccin contre la rage", "Dr Dumas", LocalDate.now(), LocalDateTime.of(2026, 12, 12,9,30), healthRecord.id());
        vaccine2 = new VaccineRequestDto("Fizer", "Vaccin contre la polio", "Dr Dumas", LocalDate.now(), LocalDateTime.of(2026, 12, 12,9,30), healthRecord.id());
        vaccine3 = new VaccineRequestDto("Props", "Vaccin contre ebola", "Dr Dumas", LocalDate.now(), LocalDateTime.of(2026, 12, 12,9,30), healthRecord.id());
        vaccine4 = new VaccineRequestDto("Stacy", "Vaccin contre la grippe", "Dr Dumas", LocalDate.now(), LocalDateTime.of(2026, 12, 12,9,30), healthRecord.id());

        vaccineService.create(vaccine1);
        vaccineService.create(vaccine2);
        vaccineService.create(vaccine3);
        vaccineService.create(vaccine4);

        appointment1 = new AppointmentRequestDto("Rendez-vous Véterinaire", LocalDateTime.of(2026, 12, 12,9,30), duration, userId);
        appointment2 = new AppointmentRequestDto("Rendez-vous Véterinaire", LocalDateTime.of(2026, 12, 12,9,30), duration, userId);
        appointment3 = new AppointmentRequestDto("Rendez-vous Véterinaire", LocalDateTime.of(2026, 12, 12,9,30), duration, userId);
        appointment4 = new AppointmentRequestDto("Rendez-vous Véterinaire", LocalDateTime.of(2026, 12, 12,9,30), duration, userId);

        appointmentService.create(appointment1);
        appointmentService.create(appointment2);
        appointmentService.create(appointment3);
        appointmentService.create(appointment4);
    }

    @Test
    @Transactional
    void shouldGetAgenda() {
        AgendaResponseDto agenda = agendaService.getDateDuringPeriod(userId, LocalDateTime.of(2026, 12, 12,9,30));

        assertThat(agenda.reminderList()).hasSize(4);
        assertThat(agenda.appointmentList()).hasSize(4);

        assertThat(agenda.reminderList().get(2).reminderDate()).isEqualTo(LocalDateTime.of(2026, 12, 12,9,30));
        assertThat(agenda.appointmentList().get(2).dateTime()).isEqualTo(LocalDateTime.of(2026, 12, 12,9,30));
    }
}
