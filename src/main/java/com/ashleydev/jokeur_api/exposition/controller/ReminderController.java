package com.ashleydev.jokeur_api.exposition.controller;

import com.ashleydev.jokeur_api.domain.services.ReminderService;
import com.ashleydev.jokeur_api.exposition.dtos.ReminderResponseDto;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reminder")
@AllArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @GetMapping()
    public ResponseEntity<List<ReminderResponseDto>> getReminders(@PathParam(value = "idOwner") Long idOwner){
        List<ReminderResponseDto> response = reminderService.getReminders(idOwner);
        return ResponseEntity.ok(response);
    }
}
