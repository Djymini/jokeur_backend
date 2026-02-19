package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.config.JokeurProperties;
import com.ashleydev.jokeur_api.domain.rules.NotificationRssRules;
import com.ashleydev.jokeur_api.exceptions.rss.RssReadException;
import com.ashleydev.jokeur_api.exposition.dtos.notification.NotificationResponseDto;
import com.ashleydev.jokeur_api.mappers.NotificationMapper;
import com.ashleydev.jokeur_api.persistence.entities.NotificationEntity;
import com.ashleydev.jokeur_api.persistence.repositories.NotificationRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class NotificationRssService {

  private final NotificationRepository notificationRepository;
  private final JokeurProperties jokeurProperties;

  public List<NotificationResponseDto> getAllNotifications() {
    return notificationRepository.getNotifications().stream().map(NotificationMapper::toDto).toList();
  }

  /******************************** Traitement Scheduler *************************** */

  /**
   * cette methode permet de syncroniser la table notification avec flux rss
   */
  public void importRssNotifications() {
    List<SyndEntry> entries = readFeed(jokeurProperties.getRssUrl());

    entries
      .stream()
      .filter((e -> !isArticlePresent(e)))
      .filter(NotificationRssRules::articleAboutAnimal)
      .map(NotificationMapper::toEntity)
      .forEach(notificationRepository::save);
  }

  private boolean isArticlePresent(SyndEntry entry) {
    String link = entry.getLink();
    Optional<NotificationEntity> notificationEntity = notificationRepository.findByLink(link);
    return notificationEntity.isPresent();
  }

  public List<SyndEntry> readFeed(String feedUrl) {
    try {
      URL url = new URL(feedUrl);
      SyndFeedInput input = new SyndFeedInput();
      SyndFeed feed = input.build(new XmlReader(url));
      return feed.getEntries();
    } catch (Exception e) {
      throw new RssReadException("Erreur lecture RSS", e);
    }
  }
}
