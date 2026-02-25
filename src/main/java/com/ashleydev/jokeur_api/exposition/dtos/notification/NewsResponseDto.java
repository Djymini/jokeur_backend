package com.ashleydev.jokeur_api.exposition.dtos.notification;

import java.time.LocalDateTime;

public record NewsResponseDto(Long id, String title, String summary, String link, LocalDateTime publishedAt, boolean sent) {}
