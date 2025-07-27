package com.project.cleanarch.eventapi.core.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.project.cleanarch.eventapi.core.domain.model.Event;
import com.project.cleanarch.eventapi.core.domain.exception.BusinessException;
import com.project.cleanarch.eventapi.core.domain.service.EventDomainService;
import com.project.cleanarch.eventapi.core.gateway.EventRepository;
import com.project.cleanarch.eventapi.infraestruct.mapper.EventMapper;
import com.project.cleanarch.eventapi.infraestruct.persistence.entities.EventEntity;

public class RetrieverEventByIdUseCaseImpl implements RetrieverEventByIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RetrieverEventByIdUseCaseImpl.class);

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventDomainService eventDomainService;

    public RetrieverEventByIdUseCaseImpl(EventRepository eventRepository, EventMapper eventMapper, EventDomainService eventDomainService) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.eventDomainService = eventDomainService;
    }

    @Override
    public Event execute(Long id) {
        logger.info("Buscando evento por ID: id={}", id);

        try {
            eventDomainService.validateId(id);
            
            EventEntity entity = findEventById(id);
            Event event = eventMapper.toModel(entity);
            
            logger.info("Evento encontrado: id={}, identifier={}", id, event.identifier());
            return event;

        } catch (BusinessException e) {
            logger.error("Erro de negócio ao buscar evento: id={}, error={}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao buscar evento: id={}, error={}", id, e.getMessage(), e);
            throw new BusinessException("event.retrieval.failed");
        }
    }

    private EventEntity findEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Evento não encontrado: id={}", id);
                    return new BusinessException("event.not.found", id);
                });
    }
}