package com.project.cleanarch.eventapi.infraestruct.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.project.cleanarch.eventapi.core.domain.model.Event;
import com.project.cleanarch.eventapi.core.usecases.CancellerEventUseCase;
import com.project.cleanarch.eventapi.core.usecases.CreatorEventUseCase;
import com.project.cleanarch.eventapi.core.usecases.RetrieverAllEventsUseCase;
import com.project.cleanarch.eventapi.core.usecases.RetrieverEventByIdUseCase;
import com.project.cleanarch.eventapi.core.usecases.RetrieverEventsByFilterUseCase;
import com.project.cleanarch.eventapi.core.usecases.UpdateEventUseCase;
import com.project.cleanarch.eventapi.infraestruct.dtos.EventDTO;
import com.project.cleanarch.eventapi.infraestruct.dtos.EventFilterDTO;
import com.project.cleanarch.eventapi.infraestruct.mapper.EventMapper;
import com.project.cleanarch.eventapi.infraestruct.presentation.constants.ApiConstants;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller REST para eventos - Camada de Apresentação
 * Orquestra os casos de uso segregados e converte entre DTOs e Domain Models
 * Segue os princípios da Clean Architecture com ISP (Interface Segregation Principle)
 */
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final CreatorEventUseCase creatorEventUseCase;
    private final RetrieverEventByIdUseCase findEventByIdUseCase;
    private final RetrieverAllEventsUseCase findAllEventsUseCase;
    private final RetrieverEventsByFilterUseCase findByFilterUseCase;
    private final UpdateEventUseCase updateEventUseCase;
    private final CancellerEventUseCase cancelEventUseCase;
    private final EventMapper eventMapper;

    @PostMapping(produces = ApiConstants.Headers.CONTENT_NEGOTIATION_V1)
    @ResponseStatus(HttpStatus.CREATED)
    public EventDTO create(@Valid @RequestBody EventDTO dto) {
        Event event = convertToDomain(dto);
        Event createdEvent = creatorEventUseCase.execute(event);
        return convertToDto(createdEvent);
    }

    @GetMapping(value = "/{id}", produces = ApiConstants.Headers.CONTENT_NEGOTIATION_V1)
    @ResponseStatus(HttpStatus.OK)
    public EventDTO findById(@PathVariable Long id) {
        Event event = findEventByIdUseCase.execute(id);
        return convertToDto(event);
    }

    @GetMapping(produces = ApiConstants.Headers.CONTENT_NEGOTIATION_V1)
    @ResponseStatus(HttpStatus.OK)
    public Page<EventDTO> findAll(Pageable pageable) {
        return findAllEventsUseCase.execute(pageable)
                .map(this::convertToDto);
    }

    @GetMapping(value = "/search", produces = ApiConstants.Headers.CONTENT_NEGOTIATION_V1)
    @ResponseStatus(HttpStatus.OK)
    public Page<EventDTO> findByFilters(@RequestBody EventFilterDTO filterDTO, Pageable pageable) {
        return findByFilterUseCase.execute(filterDTO, pageable)
                .map(this::convertToDto);
    }

    @PutMapping(value = "/{id}", produces = ApiConstants.Headers.CONTENT_NEGOTIATION_V1)
    @ResponseStatus(HttpStatus.OK)
    public EventDTO update(@PathVariable Long id, @Valid @RequestBody EventDTO dto) {
        Event event = convertToDomain(dto);
        Event updatedEvent = updateEventUseCase.execute(id, event);
        return convertToDto(updatedEvent);
    }

    @PatchMapping(value = "/{id}/cancel", produces = ApiConstants.Headers.CONTENT_NEGOTIATION_V1)
    @ResponseStatus(HttpStatus.OK)
    public void cancelEvent(@PathVariable Long id, @RequestBody EventDTO dto) {
        cancelEventUseCase.execute(id, dto.getCancelReason());
    }

    private Event convertToDomain(EventDTO dto) {
        return eventMapper.toModel(dto);
    }

    private EventDTO convertToDto(Event event) {
        return eventMapper.fromModel(event);
    }
}