package com.project.cleanarch.eventapi.infraestruct.gateway;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import com.project.cleanarch.eventapi.core.gateway.EventRepository;
import com.project.cleanarch.eventapi.infraestruct.dtos.EventFilterDTO;
import com.project.cleanarch.eventapi.infraestruct.persistence.entities.EventEntity;
import com.project.cleanarch.eventapi.infraestruct.persistence.repositories.EventJpaRepository;
import com.project.cleanarch.eventapi.infraestruct.persistence.repositories.EventSpecification;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventRepositoryImpl implements EventRepository {

    private final EventJpaRepository jpaRepository;

    @Override
    public EventEntity save(EventEntity entity) {
        return jpaRepository.save(entity);
    }

    @Override
    public Optional<EventEntity> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Page<EventEntity> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public boolean existsByIdentifier(String identifier) {
        return jpaRepository.existsByIdentifier(identifier);
    }

    @Override
    public Page<EventEntity> findByFilters(EventFilterDTO filters, Pageable pageable) {
        return jpaRepository.findAll(EventSpecification.of(filters).build(), pageable);
    }
} 