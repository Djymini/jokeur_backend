package com.ashleydev.jokeur_api.scheduler;

import com.ashleydev.jokeur_api.domain.services.NotificationRssService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@EnableScheduling
public class RssScheduler {

  private final NotificationRssService notificationRssService;

    @Scheduled(cron = "0 */2 * * * *")
  //@Scheduled(cron = "0 0 */24 * * *") // toutes les 24+h
  public void fetchRss() {
    notificationRssService.importRssNotifications();
  }
}
