package com.ashleydev.jokeur_api.exposition.dtos.appointment;

import java.time.LocalDateTime;

public record AppointmentResponseDto(Long id, String reason, LocalDateTime dateTime) {}
