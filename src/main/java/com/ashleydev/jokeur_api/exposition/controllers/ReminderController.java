package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.ReminderService;
import com.ashleydev.jokeur_api.exposition.dtos.reminder.ReminderResponseDto;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reminder")
@AllArgsConstructor
public class ReminderController {

  private final ReminderService reminderService;

  @GetMapping
  public ResponseEntity<Page<ReminderResponseDto>> getReminders(
    @PathParam(value = "userId") Long userId,
    @PageableDefault(sort = "id") Pageable pageable
  ) {
    Page<ReminderResponseDto> response = reminderService.getReminders(userId, pageable);
    return ResponseEntity.ok(response);
  }
}
