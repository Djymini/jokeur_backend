package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.AgendaService;
import com.ashleydev.jokeur_api.exposition.dtos.agenda.AgendaResponseDto;
import jakarta.websocket.server.PathParam;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agenda")
@AllArgsConstructor
public class AgendaController {

  private final AgendaService agendaService;

  @GetMapping
  public ResponseEntity<AgendaResponseDto> getAgendat(@PathParam(value = "userId") Long userId, @RequestBody LocalDateTime date) {
    return ResponseEntity.ok(agendaService.getDateDuringPeriod(userId, date));
  }
}
