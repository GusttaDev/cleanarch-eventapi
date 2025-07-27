package com.project.cleanarch.eventapi.core.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.project.cleanarch.eventapi.core.domain.model.Event;
import com.project.cleanarch.eventapi.core.domain.exception.BusinessException;
import com.project.cleanarch.eventapi.core.domain.service.EventDomainService;
import com.project.cleanarch.eventapi.core.gateway.EventRepository;
import com.project.cleanarch.eventapi.infraestruct.mapper.EventMapper;
import com.project.cleanarch.eventapi.infraestruct.persistence.entities.EventEntity;

public class UpdateEventUseCaseImpl implements UpdateEventUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UpdateEventUseCaseImpl.class);

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventDomainService eventDomainService;

    public UpdateEventUseCaseImpl(EventRepository eventRepository, EventMapper eventMapper, EventDomainService eventDomainService) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.eventDomainService = eventDomainService;
    }

    @Override
    public Event execute(Long id, Event event) {
        logger.info("Iniciando atualização de evento: id={}, identifier={}, name={}",
                    id, event.identifier(), event.name());

        try {
            eventDomainService.validateEventUpdate(id, event);
            
            EventEntity entity = findEventById(id);
            updateEventEntity(entity, event);
            
            EventEntity updatedEntity = eventRepository.save(entity);
            Event updatedEvent = eventMapper.toModel(updatedEntity);
            
            logger.info("Evento atualizado com sucesso: id={}, identifier={}", id, updatedEvent.identifier());
            return updatedEvent;

        } catch (BusinessException e) {
            logger.error("Erro de negócio ao atualizar evento: id={}, error={}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao atualizar evento: id={}, error={}", id, e.getMessage(), e);
            throw new BusinessException("event.update.failed");
        }
    }

    private EventEntity findEventById(Long id) {
        return eventRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Evento não encontrado para atualização: id={}", id);
                return new BusinessException("event.update.not.found", id);
            });
    }

    private void updateEventEntity(EventEntity entity, Event event) {
        eventMapper.updateEntity(entity, event);
    }
} 