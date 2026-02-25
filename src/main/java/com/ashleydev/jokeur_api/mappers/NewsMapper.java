package com.ashleydev.jokeur_api.mappers;

import com.ashleydev.jokeur_api.exposition.dtos.notification.NewsResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.NewsEntity;
import com.rometools.rome.feed.synd.SyndEntry;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class NewsMapper {

  public static NewsResponseDto toDto(NewsEntity entity) {
    return new NewsResponseDto(entity.getId(), entity.getTitle(), entity.getSummary(), entity.getLink(), entity.getPublishedAt(), entity.isSent());
  }

  public static NewsEntity toEntity(SyndEntry entry) {
    return NewsEntity.builder()
      .title(entry.getTitle())
      .summary(entry.getDescription() != null ? entry.getDescription().getValue() : "")
      .link(entry.getLink())
      .publishedAt(
        entry.getPublishedDate() != null ? entry.getPublishedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : LocalDateTime.now()
      )
      .sent(false)
      .build();
  }
}
