package com.project.cleanarch.eventapi.infraestruct.persistence.entities;

import java.time.LocalDateTime;

import com.project.cleanarch.eventapi.core.domain.enums.EventStatusEnum;
import com.project.cleanarch.eventapi.core.domain.enums.EventType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "events")
public class EventEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false, unique = true)
    private String identifier;
    
    @Column(nullable = false)
    private LocalDateTime startAt;
    
    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatusEnum status;

    @Column(name = "cancel_reason")
    private String cancelReason;
}
