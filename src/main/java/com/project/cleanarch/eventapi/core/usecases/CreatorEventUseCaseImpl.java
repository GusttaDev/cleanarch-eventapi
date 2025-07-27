package com.project.cleanarch.eventapi.core.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.project.cleanarch.eventapi.core.domain.model.Event;
import com.project.cleanarch.eventapi.core.domain.exception.BusinessException;
import com.project.cleanarch.eventapi.core.domain.service.EventDomainService;
import com.project.cleanarch.eventapi.core.gateway.EventRepository;
import com.project.cleanarch.eventapi.infraestruct.mapper.EventMapper;
import com.project.cleanarch.eventapi.infraestruct.persistence.entities.EventEntity;

public class CreatorEventUseCaseImpl implements CreatorEventUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreatorEventUseCaseImpl.class);

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventDomainService eventDomainService;

    public CreatorEventUseCaseImpl(EventRepository eventRepository, EventMapper eventMapper, EventDomainService eventDomainService) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.eventDomainService = eventDomainService;
    }

    @Override
    public Event execute(Event event) {
        logger.info("Iniciando criação de evento: identifier={}, name={}, capacity={}",
                    event.identifier(), event.name(), event.capacity());

        try {
            eventDomainService.validateEventCreation(event);
            
            EventEntity entity = eventMapper.toEntity(event);
            EventEntity savedEntity = eventRepository.save(entity);
            Event savedEvent = eventMapper.toModel(savedEntity);
            
            logger.info("Evento criado com sucesso: identifier={}", savedEvent.identifier());
            return savedEvent;

        } catch (BusinessException e) {
            logger.error("Erro de negócio ao criar evento: identifier={}, error={}",
                         event.identifier(), e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao criar evento: identifier={}, error={}", 
                         event.identifier(), e.getMessage(), e);
            throw new BusinessException("event.creation.failed");
        }
    }
}