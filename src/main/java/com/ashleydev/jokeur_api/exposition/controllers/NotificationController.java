package com.ashleydev.jokeur_api.exposition.controllers;

import com.ashleydev.jokeur_api.domain.services.NotificationRssService;
import com.ashleydev.jokeur_api.exposition.dtos.notification.NotificationResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/news")
public class NotificationController {

  private NotificationRssService notificationRssService;

  @GetMapping
  public Page<NotificationResponseDto> getAllNotifications(
    @PageableDefault(size = 10, sort = "publishedAt", direction = Sort.Direction.DESC) Pageable pageable
  ) {
    return notificationRssService.getAllNotifications(pageable);
  }
}
