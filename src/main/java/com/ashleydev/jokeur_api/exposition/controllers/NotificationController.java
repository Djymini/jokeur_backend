package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.NotificationRssService;
import com.ashleydev.jokeur_api.exposition.dtos.notification.NotificationResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

  private NotificationRssService notificationRssService;

  @GetMapping
  public ResponseEntity<List<NotificationResponseDto>> getNotifications() {
    return ResponseEntity.ok(notificationRssService.getAllNotifications());
  }
}
