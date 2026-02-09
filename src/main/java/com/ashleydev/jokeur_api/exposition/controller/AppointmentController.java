package com.ashleydev.jokeur_api.exposition.controller;

import com.ashleydev.jokeur_api.domain.services.AppointmentService;
import com.ashleydev.jokeur_api.exposition.dtos.AppointmentResponseDto;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/appointment")
@AllArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping()
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmenet(@PathParam(value ="idOwner") Long idOwner){
        List<AppointmentResponseDto> response = appointmentService.getAllAppointement(idOwner);
        return ResponseEntity.ok(response);
    }
}
