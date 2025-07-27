package com.project.cleanarch.eventapi.core.gateway;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.project.cleanarch.eventapi.infraestruct.dtos.EventFilterDTO;
import com.project.cleanarch.eventapi.infraestruct.persistence.entities.EventEntity;

public interface EventRepository {
    EventEntity save(EventEntity entity);
    Optional<EventEntity> findById(Long id);
    Page<EventEntity> findAll(Pageable pageable);
    boolean existsByIdentifier(String identifier);
    Page<EventEntity> findByFilters(EventFilterDTO eventFilterDTO, Pageable pageable);
}