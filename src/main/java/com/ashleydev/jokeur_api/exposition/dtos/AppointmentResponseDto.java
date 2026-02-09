package com.ashleydev.jokeur_api.exposition.dtos;


import java.time.LocalDateTime;

public record AppointmentResponseDto(
         Long id,
         String reason,
         LocalDateTime dateTime
) {}
