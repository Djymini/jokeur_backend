package com.ashleydev.jokeur_api.scheduler;

import com.ashleydev.jokeur_api.domain.services.NewsRssService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@EnableScheduling
public class RssScheduler {

  private final NewsRssService newsRssService;

  // "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
  @Scheduled(cron = "0 0/10 6-23 * * *")
  // SAUV : @Scheduled(cron = "0 0 */12 * * *")
  public void fetchRss() {
    newsRssService.importRssNews();
  }
}
