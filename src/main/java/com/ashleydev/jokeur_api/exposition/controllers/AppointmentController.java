package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.AppointmentService;
import com.ashleydev.jokeur_api.exposition.dtos.appointment.AppointmentRequestDto;
import com.ashleydev.jokeur_api.exposition.dtos.appointment.AppointmentResponseDto;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appointment")
@AllArgsConstructor
public class AppointmentController {

  private final AppointmentService appointmentService;

  @GetMapping
  public ResponseEntity<Page<AppointmentResponseDto>> getAppointmenet(
    @PathParam(value = "userId") Long userId,
    @PageableDefault(sort = "id") Pageable pageable
  ) {
    return ResponseEntity.ok(appointmentService.getAllAppointement(userId, pageable));
  }

  @PostMapping
  public ResponseEntity<AppointmentResponseDto> addAppointment(@RequestBody AppointmentRequestDto appointment) {
    AppointmentResponseDto response = appointmentService.create(appointment);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{userId}/{id}")
  public ResponseEntity<AppointmentResponseDto> modifyTreatmentById(@RequestBody AppointmentRequestDto appointment, @PathVariable Long id) {
    AppointmentResponseDto response = appointmentService.update(appointment, id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{userId}/{id}")
  public ResponseEntity<String> deleteTreatmentOfHealthRecordById(@PathVariable Long userId, @PathVariable Long id) {
    String response = appointmentService.delete(id, userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
  }
}
