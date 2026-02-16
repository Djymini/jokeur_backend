package com.ashleydev.jokeur_api.mappers;


import com.ashleydev.jokeur_api.exposition.dtos.notification.NotificationResponseDto;
import com.ashleydev.jokeur_api.persistence.entities.NotificationEntity;
import com.rometools.rome.feed.synd.SyndEntry;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class NotificationMapper {

    public static NotificationResponseDto toDto(NotificationEntity entity){
        return new NotificationResponseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getSummary(),
                entity.getLink(),
                entity.getPublishedAt(),
                entity.isSent()
        );

    }

    public static NotificationEntity toEntity(SyndEntry entry) {
        
        return NotificationEntity.builder()
                .title(entry.getTitle())
                .summary(entry.getDescription() != null
                        ? entry.getDescription().getValue()
                        : "")
                .link(entry.getLink())
                .publishedAt(
                        entry.getPublishedDate() != null
                                ? entry.getPublishedDate()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                                : LocalDateTime.now()
                )
                .sent(false)
                .build();
    }
}
