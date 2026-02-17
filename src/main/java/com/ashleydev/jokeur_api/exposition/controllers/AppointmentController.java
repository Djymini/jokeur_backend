package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.AppointmentService;
import com.ashleydev.jokeur_api.exposition.dtos.appointment.AppointmentResponseDto;
import jakarta.websocket.server.PathParam;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointment")
@AllArgsConstructor
public class AppointmentController {

  private final AppointmentService appointmentService;

  @GetMapping
  public ResponseEntity<List<AppointmentResponseDto>> getAppointmenet(@PathParam(value = "idOwner") Long idOwner) {
    List<AppointmentResponseDto> response = appointmentService.getAllAppointement(idOwner);
    return ResponseEntity.ok(response);
  }
}
