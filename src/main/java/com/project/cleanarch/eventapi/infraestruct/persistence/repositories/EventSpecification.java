package com.project.cleanarch.eventapi.infraestruct.persistence.repositories;

import org.springframework.data.jpa.domain.Specification;
import com.project.cleanarch.eventapi.infraestruct.dtos.EventFilterDTO;
import com.project.cleanarch.eventapi.infraestruct.persistence.entities.EventEntity;

import lombok.RequiredArgsConstructor;

/**
 * Builder para criar Specification combinado baseado no EventFilterDTO
 * Seguindo o padrão Spring Data JPA para Specifications
 */
@RequiredArgsConstructor(staticName = "of")
public class EventSpecification {

    private final EventFilterDTO filter;

    /**
     * Constrói uma Specification combinada baseada nos filtros fornecidos
     * Seguindo o padrão da documentação Spring Data JPA
     */
    public Specification<EventEntity> build() {
        Specification<EventEntity> spec = (root, query, cb) -> cb.conjunction();

        spec = spec.and(EventSpecifications.hasName(filter.getName()));
        spec = spec.and(EventSpecifications.hasLocation(filter.getLocation()));
        spec = spec.and(EventSpecifications.hasType(filter.getType()));
        spec = spec.and(EventSpecifications.hasStatus(filter.getStatus()));
        spec = spec.and(EventSpecifications.hasStartDateFrom(filter.getStartAtFrom()));
        spec = spec.and(EventSpecifications.hasStartDateTo(filter.getStartAtTo()));
        spec = spec.and(EventSpecifications.hasEndDateFrom(filter.getEndAtFrom()));
        spec = spec.and(EventSpecifications.hasEndDateTo(filter.getEndAtTo()));
        spec = spec.and(EventSpecifications.hasCapacityMin(filter.getCapacityMin()));
        spec = spec.and(EventSpecifications.hasCapacityMax(filter.getCapacityMax()));
        spec = spec.and(EventSpecifications.hasIdentifier(filter.getIdentifier()));

        return spec;
    }
}
