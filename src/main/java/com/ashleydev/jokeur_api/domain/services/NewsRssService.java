package com.ashleydev.jokeur_api.domain.services;

import com.ashleydev.jokeur_api.config.JokeurProperties;
import com.ashleydev.jokeur_api.domain.rules.NewsRssRules;
import com.ashleydev.jokeur_api.exceptions.rss.RssReadException;
import com.ashleydev.jokeur_api.exposition.dtos.notification.NewsResponseDto;
import com.ashleydev.jokeur_api.mappers.NewsMapper;
import com.ashleydev.jokeur_api.persistence.entities.NewsEntity;
import com.ashleydev.jokeur_api.persistence.repositories.NewsRepository;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class NewsRssService {

  private final NewsRepository newsRepository;
  private final JokeurProperties jokeurProperties;

  public Page<NewsResponseDto> getAllNews(Pageable pageable) {
    return newsRepository.findAllByOrderByPublishedAtDesc(pageable).map(NewsMapper::toDto);
  }

  /******************************** Traitement Scheduler *************************** */

  /**
   * cette methode permet de syncroniser la table notification avec flux rss
   */
  public void importRssNews() {
    List<SyndEntry> entries = readFeed(jokeurProperties.getRssUrl());

    entries
      .stream()
      .filter((e -> !isArticlePresent(e)))
      .filter(NewsRssRules::articleAboutAnimal)
      .map(NewsMapper::toEntity)
      .forEach(newsRepository::save);
  }

  private boolean isArticlePresent(SyndEntry entry) {
    String link = entry.getLink();
    Optional<NewsEntity> notificationEntity = newsRepository.findByLink(link);
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
