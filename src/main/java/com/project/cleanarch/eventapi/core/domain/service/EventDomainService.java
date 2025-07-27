package com.project.cleanarch.eventapi.core.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.project.cleanarch.eventapi.core.domain.exception.BusinessException;
import com.project.cleanarch.eventapi.core.domain.model.Event;
import com.project.cleanarch.eventapi.core.gateway.EventRepository;

/**
 * Service de Domínio para Event
 * Responsável pelas regras de negócio que envolvem múltiplas entidades
 * Segue os princípios do Domain-Driven Design
 */
public class EventDomainService {

    private static final Logger logger = LoggerFactory.getLogger(EventDomainService.class);

    private final EventRepository eventRepository;

    public EventDomainService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void validateEventCreation(Event event) {
        logger.debug("Validando criação do evento: identifier={}", event.identifier());

        validateIdentifierUniqueness(event.identifier());
        validateBusinessRules(event);
    }


    public void validateEventUpdate(Long id, Event event) {
        logger.debug("Validando atualização do evento: id={}, identifier={}", id, event.identifier());

        validateId(id);
        validateBusinessRules(event);
    }

    public void validateEventCancellation(Long id, String reason) {
        logger.debug("Validando cancelamento do evento: id={}", id);

        validateId(id);
        validateCancelReason(reason);
    }


    public void validateId(Long id) {
        if (id == null || id <= 0) {
            logger.warn("ID inválido: {}", id);
            throw new BusinessException("event.id.invalid");
        }
    }

    private void validateIdentifierUniqueness(String identifier) {
        if (eventRepository.existsByIdentifier(identifier)) {
            logger.warn("Tentativa de criar evento com identifier duplicado: identifier={}", identifier);
            throw new BusinessException("event.already.exists");
        }
    }

    private void validateBusinessRules(Event event) {
        if (!event.isValidDateRange()) {
            logger.warn("Data de início posterior à data de fim: startAt={}, endAt={}", 
                        event.startAt(), event.endAt());
            throw new BusinessException("event.invalid.date.range");
        }

        if (!event.isValidCapacity()) {
            logger.warn("Capacidade inválida: capacity={}", event.capacity());
            throw new BusinessException("event.invalid.capacity");
        }
    }

    private void validateCancelReason(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            logger.warn("Motivo do cancelamento é obrigatório");
            throw new BusinessException("event.cancel.reason.required");
        }
    }
} 