package com.ashleydev.jokeur_api.exposition.dtos.appointment;

import java.time.LocalDateTime;

public record AppointmentRequestDto(String reason, LocalDateTime dateTime, Long userId) {}
