package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.AppointmentService;
import com.ashleydev.jokeur_api.exposition.dtos.appointment.AppointmentResponseDto;
import jakarta.websocket.server.PathParam;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
  public ResponseEntity<Page<AppointmentResponseDto>> getAppointmenet(@PathParam(value = "idOwner") Long idOwner, @PageableDefault(sort = "id") Pageable pageable) {
    return ResponseEntity.ok(appointmentService.getAllAppointement(idOwner, pageable));
  }
}
