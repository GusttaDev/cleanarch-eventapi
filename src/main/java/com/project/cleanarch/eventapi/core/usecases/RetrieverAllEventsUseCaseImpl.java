package com.project.cleanarch.eventapi.core.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.project.cleanarch.eventapi.core.domain.model.Event;
import com.project.cleanarch.eventapi.core.domain.exception.BusinessException;
import com.project.cleanarch.eventapi.core.gateway.EventRepository;
import com.project.cleanarch.eventapi.infraestruct.mapper.EventMapper;
import com.project.cleanarch.eventapi.infraestruct.persistence.entities.EventEntity;

public class RetrieverAllEventsUseCaseImpl implements RetrieverAllEventsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RetrieverAllEventsUseCaseImpl.class);

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public RetrieverAllEventsUseCaseImpl(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public Page<Event> execute(Pageable pageable) {
        logger.info("Buscando todos os eventos: page={}, size={}", 
                    pageable.getPageNumber(), pageable.getPageSize());

        try {
            Page<EventEntity> entities = eventRepository.findAll(pageable);
            Page<Event> events = entities.map(eventMapper::toModel);
            
            logger.info("Eventos encontrados: total={}, page={}", 
                        events.getTotalElements(), events.getNumber());
            return events;

        } catch (Exception e) {
            logger.error("Erro ao buscar eventos: error={}", e.getMessage(), e);
            throw new BusinessException("event.retrieval.failed");
        }
    }
}