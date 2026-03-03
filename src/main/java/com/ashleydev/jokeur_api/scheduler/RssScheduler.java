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

  @Scheduled(cron = "0 0 */12 * * *")
  public void fetchRss() {
    newsRssService.importRssNews();
  }
}
