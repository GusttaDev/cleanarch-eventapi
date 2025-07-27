package com.project.cleanarch.eventapi.core.domain.model;

import java.time.LocalDateTime;

import com.project.cleanarch.eventapi.core.domain.enums.EventStatusEnum;
import com.project.cleanarch.eventapi.core.domain.enums.EventType;

/**
 * Modelo de domínio Event
 * Contém apenas regras de negócio e comportamento de domínio
 * Não possui anotações de validação ou infraestrutura
 */
public record Event(
        String name,
        String description,
        String identifier,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String location,
        int capacity,
        EventType type,
        EventStatusEnum status,
        String cancelReason
) {
    

    public boolean isValidDateRange() {
        return startAt != null && endAt != null && !startAt.isAfter(endAt());
    }
    

    public boolean isValidCapacity() {
        return capacity > 0;
    }


}
