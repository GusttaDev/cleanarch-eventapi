package com.project.cleanarch.eventapi.core.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.project.cleanarch.eventapi.core.domain.model.Event;
import com.project.cleanarch.eventapi.core.domain.exception.BusinessException;
import com.project.cleanarch.eventapi.core.gateway.EventRepository;
import com.project.cleanarch.eventapi.infraestruct.dtos.EventFilterDTO;
import com.project.cleanarch.eventapi.infraestruct.mapper.EventMapper;
import com.project.cleanarch.eventapi.infraestruct.persistence.entities.EventEntity;

public class RetrieverEventsByFilterUseCaseImpl implements RetrieverEventsByFilterUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RetrieverEventsByFilterUseCaseImpl.class);

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public RetrieverEventsByFilterUseCaseImpl(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public Page<Event> execute(EventFilterDTO filterDTO, Pageable pageable) {
        logger.info("Buscando eventos por filtros: name={}, location={}, type={}, status={}, page={}, size={}",
                    filterDTO.getName(), filterDTO.getLocation(), filterDTO.getType(), filterDTO.getStatus(),
                    pageable.getPageNumber(), pageable.getPageSize());

        try {
            Page<EventEntity> entities = eventRepository.findByFilters(filterDTO, pageable);
            Page<Event> events = entities.map(eventMapper::toModel);
            
            logger.info("Eventos encontrados com filtros: total={}, page={}", events.getTotalElements(), events.getNumber());

            return events;

        } catch (Exception e) {
            logger.error("Erro ao buscar eventos por filtros: error={}", e.getMessage(), e);
            throw new BusinessException("event.filter.retrieval.failed");
        }
    }
}