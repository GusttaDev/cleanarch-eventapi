package com.project.cleanarch.eventapi.infraestruct.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.project.cleanarch.eventapi.core.domain.model.Event;
import com.project.cleanarch.eventapi.infraestruct.dtos.EventDTO;
import com.project.cleanarch.eventapi.infraestruct.persistence.entities.EventEntity;

/**
 * Mapper para Event - Camada de Infraestrutura
 * Responsável pela conversão entre as diferentes representações do Event
 * Utiliza MapStruct conforme preferência do usuário (toModel/toEntity)
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {

    /**
     * Converte de Entity para Domain Model
     */
    Event toModel(EventEntity entity);

    /**
     * Converte de Domain Model para Entity (para criação)
     */
    @Mapping(target = "id", ignore = true)
    EventEntity toEntity(Event event);

    /**
     * Converte de DTO para Domain Model
     */
    Event toModel(EventDTO dto);

    /**
     * Converte de Domain Model para DTO
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EventDTO fromModel(Event event);

    /**
     * Converte de Entity para DTO
     */
    EventDTO fromModel(EventEntity entity);

    /**
     * Atualiza Entity existente com dados do Domain Model (para updates)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntity(@MappingTarget EventEntity entity, Event event);
}
