package com.project.cleanarch.eventapi.core.usecases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.project.cleanarch.eventapi.core.domain.enums.EventStatusEnum;
import com.project.cleanarch.eventapi.core.domain.exception.BusinessException;
import com.project.cleanarch.eventapi.core.domain.service.EventDomainService;
import com.project.cleanarch.eventapi.core.gateway.EventRepository;
import com.project.cleanarch.eventapi.infraestruct.persistence.entities.EventEntity;

public class CancellerEventUseCaseImpl implements CancellerEventUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CancellerEventUseCaseImpl.class);

    private final EventRepository eventRepository;
    private final EventDomainService eventDomainService;

    public CancellerEventUseCaseImpl(EventRepository eventRepository, EventDomainService eventDomainService) {
        this.eventRepository = eventRepository;
        this.eventDomainService = eventDomainService;
    }

    @Override
    public void execute(Long id, String reason) {
        logger.info("Iniciando cancelamento de evento: id={}, motivo={}", id, reason);
        
        try {
            eventDomainService.validateEventCancellation(id, reason);
            
            EventEntity entity = findEventById(id);
            cancelEvent(entity, reason);
            
            logger.info("Evento cancelado com sucesso: id={}, motivo={}", id, reason);
            
        } catch (BusinessException e) {
            logger.error("Erro de negócio ao cancelar evento: id={}, error={}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao cancelar evento: id={}, error={}", id, e.getMessage(), e);
            throw new BusinessException("event.cancellation.failed");
        }
    }

    private EventEntity findEventById(Long id) {
        return eventRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Evento não encontrado para cancelamento: id={}", id);
                return new BusinessException("event.delete.not.found", id);
            });
    }

    private void cancelEvent(EventEntity entity, String reason) {
        entity.setStatus(EventStatusEnum.CANCELLED);
        entity.setCancelReason(reason);
        eventRepository.save(entity);
    }
} 