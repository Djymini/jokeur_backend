package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.AgendaService;
import com.ashleydev.jokeur_api.exposition.dtos.agenda.AgendaResponseDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agenda")
@AllArgsConstructor
public class AgendaController {

  private final AgendaService agendaService;

  @GetMapping("/{userId}")
  public ResponseEntity<AgendaResponseDto> getAgendat(
    @PathVariable Long userId,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date
  ) {
    return ResponseEntity.ok(agendaService.getDateDuringPeriod(userId, date));
  }
}
