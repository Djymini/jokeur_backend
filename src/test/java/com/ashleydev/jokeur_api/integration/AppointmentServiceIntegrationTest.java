package com.ashleydev.jokeur_api.integration;

import com.ashleydev.jokeur_api.domain.services.AppointmentService;
import com.ashleydev.jokeur_api.exposition.dtos.appointment.AppointmentRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.appointment.AppointmentResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.AppointmentEntity;
import com.ashleydev.jokeur_api.persistence.entities.UserEntity;
import com.ashleydev.jokeur_api.persistence.repositories.AppoinmentRepository;
import com.ashleydev.jokeur_api.persistence.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.profiles.active=integration")
@ActiveProfiles("integration")
@Transactional
public class AppointmentServiceIntegrationTest {
    @Autowired
    private AppointmentService appointmentService;

    @Autowired private AppoinmentRepository appoinmentRepository;
    @Autowired private UserRepository userRepository;

    private Long userId;
    private Long duration = 30L;

    private AppointmentRequestDto appointment1;
    private AppointmentRequestDto appointment2;
    private AppointmentRequestDto appointment3;
    private AppointmentRequestDto appointment4;

    private AppointmentResponseDto appointmentCreated1;
    private AppointmentResponseDto appointmentCreated2;
    private AppointmentResponseDto appointmentCreated3;
    private AppointmentResponseDto appointmentCreated4;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        appoinmentRepository.deleteAll();

        UserEntity user = new UserEntity();
        user.setEmail("owner@test.com");
        user.setFirstname("Owner");
        user.setName("Test");
        user.setPhoneNumber("0600000000");
        user.setPassword("P@ssword1234");
        userId = userRepository.save(user).getId();

        appointment1 = new AppointmentRequestDto("Rendez-vous Véterinaire", LocalDateTime.of(2026, 12, 12,9,30), duration, userId);
        appointment2 = new AppointmentRequestDto("Rendez-vous Véterinaire", LocalDateTime.of(2026, 12, 12,9,30), duration, userId);
        appointment3 = new AppointmentRequestDto("Rendez-vous Véterinaire", LocalDateTime.of(2026, 12, 12,9,30), duration, userId);
        appointment4 = new AppointmentRequestDto("Rendez-vous Véterinaire", LocalDateTime.of(2026, 12, 12,9,30), duration, userId);

        appointmentCreated1 = appointmentService.create(appointment1);
        appointmentCreated2 = appointmentService.create(appointment2);
        appointmentCreated3 = appointmentService.create(appointment3);
        appointmentCreated4 = appointmentService.create(appointment4);
    }

    @Test
    @Transactional
    void shouldCreateAppointments() {
        List<AppointmentEntity> allAppointment = appoinmentRepository.findAllByUserId(userId);

        assertThat(allAppointment).hasSize(4);

        AppointmentEntity persisted = allAppointment.get(2);
        assertThat(persisted.getId()).isEqualTo(appointmentCreated3.id());
        assertThat(persisted.getDateTime()).isEqualTo(LocalDateTime.of(2026, 12, 12,9,30));
        assertThat(persisted.getReason()).isEqualTo(appointment3.reason());
    }

    @Test
    @Transactional
    void shouldThrowForBadCreation() {
        AppointmentRequestDto appointmentInvalid = new AppointmentRequestDto("Rendez-vous Véterinaire", LocalDateTime.of(2026, 12, 12,9,30), duration, 999L);

        Exception exception1 = assertThrows(RuntimeException.class,
                () -> appointmentService.create(appointmentInvalid));

        assertTrue(exception1.getMessage().contains("User not found with id: 999"));
    }

    @Test
    @Transactional
    void shouldGetTreatmentsById() {
        AppointmentEntity treatmentPersisted = appoinmentRepository.findById(appointmentCreated1.id()).get();
        AppointmentResponseDto response = appointmentService.getById(appointmentCreated1.id());

        assertThat(treatmentPersisted.getId()).isEqualTo(response.id());
        assertThat(treatmentPersisted.getReason()).isEqualTo(response.reason());
    }

    @Test
    @Transactional
    void shouldThrowForBadRequestGetMeasure() {
        Long invalidId = 999L;
        Exception exception1 = assertThrows(RuntimeException.class,
                () -> appointmentService.getById(invalidId));

        assertTrue(exception1.getMessage().contains("Appointment not found with id: " + invalidId));
    }

    @Test
    @Transactional
    void shouldGetUpdate() {
        AppointmentEntity persisted = appoinmentRepository.findById(appointmentCreated1.id()).get();
        AppointmentRequestDto request = new AppointmentRequestDto("Rendez-vous changé", LocalDateTime.of(2026, 12, 12,9,30), duration, userId);

        appointmentService.update(request, appointmentCreated1.id());
        AppointmentResponseDto test = appointmentService.getById(appointmentCreated1.id());

        assertThat(test.id()).isEqualTo(persisted.getId());
        assertThat(test.reason()).isEqualTo(request.reason());
        assertThat(test.dateTime()).isEqualTo(request.dateTime());
    }

    @Test
    @Transactional
    void shouldThrowForBadUpdateRequest() {
        AppointmentRequestDto request = new AppointmentRequestDto("Rendez-vous changé", LocalDateTime.of(2026, 12, 12,9,30), duration, userId);
        AppointmentRequestDto invalidRequest1 = new AppointmentRequestDto("Rendez-vous changé", LocalDateTime.of(2026, 12, 12,9,30), duration, 999L);

        Exception ex = assertThrows(RuntimeException.class,
                () -> appointmentService.update(request, 999L));

        assertTrue(ex.getMessage().contains("Appointment not found with id: 999"));

        Exception ex2 = assertThrows(RuntimeException.class,
                () -> appointmentService.update(invalidRequest1, appointmentCreated1.id()));

        assertTrue(ex2.getMessage().contains("User not found with id: 999"));
    }

    @Test
    @Transactional
    void shouldGetDelete() {
        List<AppointmentEntity> valueToTest = appoinmentRepository.findAll();
        String response = appointmentService.delete(appointmentCreated2.id(), userId);
        List<AppointmentEntity> persisted = appoinmentRepository.findAll();

        assertThat(response).isEqualTo("Appointment : " + appointmentCreated2.id() + " is deleted");

        assertThat(persisted.size()).isEqualTo(valueToTest.size()-1);
        assertThat(persisted.get(1).getReason()).isEqualTo(appointmentCreated3.reason());
        assertThat(persisted.get(1).getDateTime()).isEqualTo(appointmentCreated3.dateTime());
    }
}
