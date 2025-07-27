package com.project.cleanarch.eventapi.infraestruct.persistence.repositories;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;
import com.project.cleanarch.eventapi.core.domain.enums.EventStatusEnum;
import com.project.cleanarch.eventapi.core.domain.enums.EventType;
import com.project.cleanarch.eventapi.infraestruct.persistence.entities.EventEntity;

import lombok.experimental.UtilityClass;

/**
 * Specifications para {@link com.project.infrastructure.persistence.entity.EventEntity}
 * seguindo o padrão do <a href="https://docs.spring.io/spring-data/jpa/reference/jpa/specifications.html">Spring Data JPA Specifications</a>.
 *
 * <p>Utilize esta classe para compor filtros dinâmicos com {@link org.springframework.data.jpa.domain.Specification}.
 *
 * @author Gustavo
 * @since 1.0
 */
@UtilityClass
public final class EventSpecifications {

    private static final String NAME_FIELD = "name";
    private static final String LOCATION_FIELD = "location";
    private static final String TYPE_FIELD = "type";
    private static final String STATUS_FIELD = "status";
    private static final String START_AT_FIELD = "startAt";
    private static final String END_AT_FIELD = "endAt";
    private static final String CAPACITY_FIELD = "capacity";
    private static final String IDENTIFIER_FIELD = "identifier";

    public static Specification<EventEntity> hasName(String name) {
        return createStringLikeSpecification(name, NAME_FIELD);
    }

    public static Specification<EventEntity> hasLocation(String location) {
        return createStringLikeSpecification(location, LOCATION_FIELD);
    }

    public static Specification<EventEntity> hasType(EventType type) {
        return createEnumSpecification(type, TYPE_FIELD);
    }

    public static Specification<EventEntity> hasStatus(EventStatusEnum status) {
        return createEnumSpecification(status, STATUS_FIELD);
    }

    public static Specification<EventEntity> hasStartDateFrom(LocalDateTime from) {
        return createDateFromSpecification(from, START_AT_FIELD);
    }

    public static Specification<EventEntity> hasStartDateTo(LocalDateTime to) {
        return createDateToSpecification(to, START_AT_FIELD);
    }

    public static Specification<EventEntity> hasEndDateFrom(LocalDateTime from) {
        return createDateFromSpecification(from, END_AT_FIELD);
    }

    public static Specification<EventEntity> hasEndDateTo(LocalDateTime to) {
        return createDateToSpecification(to, END_AT_FIELD);
    }

    public static Specification<EventEntity> hasCapacityMin(Integer min) {
        return createIntegerMinSpecification(min);
    }

    public static Specification<EventEntity> hasCapacityMax(Integer max) {
        return createIntegerMaxSpecification(max);
    }

    public static Specification<EventEntity> hasIdentifier(String identifier) {
        return createExactStringSpecification(identifier);
    }

    // Métodos auxiliares para criar specifications
    private static Specification<EventEntity> createStringLikeSpecification(String value, String fieldName) {
        return (root, query, cb) -> isNullOrBlank(value) 
            ? null 
            : cb.like(cb.upper(root.get(fieldName)), "%" + value.toUpperCase() + "%");
    }

    private static Specification<EventEntity> createExactStringSpecification(String value) {
        return (root, query, cb) -> isNullOrBlank(value) 
            ? null 
            : cb.equal(root.get(EventSpecifications.IDENTIFIER_FIELD), value);
    }

    private static <T extends Enum<T>> Specification<EventEntity> createEnumSpecification(T value, String fieldName) {
        return (root, query, cb) -> value == null 
            ? null 
            : cb.equal(root.get(fieldName), value);
    }

    private static Specification<EventEntity> createDateFromSpecification(LocalDateTime from, String fieldName) {
        return (root, query, cb) -> from == null 
            ? null 
            : cb.greaterThanOrEqualTo(root.get(fieldName), from);
    }

    private static Specification<EventEntity> createDateToSpecification(LocalDateTime to, String fieldName) {
        return (root, query, cb) -> to == null 
            ? null 
            : cb.lessThanOrEqualTo(root.get(fieldName), to);
    }

    private static Specification<EventEntity> createIntegerMinSpecification(Integer min) {
        return (root, query, cb) -> min == null 
            ? null 
            : cb.greaterThanOrEqualTo(root.get(EventSpecifications.CAPACITY_FIELD), min);
    }

    private static Specification<EventEntity> createIntegerMaxSpecification(Integer max) {
        return (root, query, cb) -> max == null 
            ? null 
            : cb.lessThanOrEqualTo(root.get(EventSpecifications.CAPACITY_FIELD), max);
    }

    private static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }
}