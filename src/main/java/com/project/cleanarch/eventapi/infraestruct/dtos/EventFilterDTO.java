package com.project.cleanarch.eventapi.infraestruct.dtos;

import java.time.LocalDateTime;

import com.project.cleanarch.eventapi.core.domain.enums.EventStatusEnum;
import com.project.cleanarch.eventapi.core.domain.enums.EventType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EventFilterDTO {
    private String name;
    private String location;
    private EventType type;
    private EventStatusEnum status;
    private LocalDateTime startAtFrom;
    private LocalDateTime startAtTo;
    private LocalDateTime endAtFrom;
    private LocalDateTime endAtTo;
    private Integer capacityMin;
    private Integer capacityMax;
    private String identifier;
} 