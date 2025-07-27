package com.project.cleanarch.eventapi.infraestruct.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.project.cleanarch.eventapi.core.domain.service.EventDomainService;
import com.project.cleanarch.eventapi.core.gateway.EventRepository;
import com.project.cleanarch.eventapi.core.usecases.CreatorEventUseCase;
import com.project.cleanarch.eventapi.core.usecases.CreatorEventUseCaseImpl;
import com.project.cleanarch.eventapi.core.usecases.RetrieverAllEventsUseCase;
import com.project.cleanarch.eventapi.core.usecases.RetrieverAllEventsUseCaseImpl;
import com.project.cleanarch.eventapi.core.usecases.RetrieverEventByIdUseCase;
import com.project.cleanarch.eventapi.core.usecases.RetrieverEventByIdUseCaseImpl;
import com.project.cleanarch.eventapi.core.usecases.RetrieverEventsByFilterUseCase;
import com.project.cleanarch.eventapi.core.usecases.RetrieverEventsByFilterUseCaseImpl;
import com.project.cleanarch.eventapi.core.usecases.UpdateEventUseCase;
import com.project.cleanarch.eventapi.core.usecases.UpdateEventUseCaseImpl;
import com.project.cleanarch.eventapi.core.usecases.CancellerEventUseCase;
import com.project.cleanarch.eventapi.core.usecases.CancellerEventUseCaseImpl;
import com.project.cleanarch.eventapi.infraestruct.mapper.EventMapper;

/**
 * Configuração dos Use Cases na camada de infraestrutura
 * Responsável por criar os beans e fazer a injeção de dependência
 * Mantém o core puro e independente do Spring
 */
@Configuration
public class EventUseCaseConfig {

    @Bean
    public CreatorEventUseCase creatorEventUseCase(EventRepository eventRepository, EventMapper eventMapper, EventDomainService eventDomainService) {
        return new CreatorEventUseCaseImpl(eventRepository, eventMapper, eventDomainService);
    }

    @Bean
    public UpdateEventUseCase updateEventUseCase(EventRepository eventRepository, EventMapper eventMapper, EventDomainService eventDomainService) {
        return new UpdateEventUseCaseImpl(eventRepository, eventMapper, eventDomainService);
    }

    @Bean
    public RetrieverEventByIdUseCase retrieverEventByIdUseCase(EventRepository eventRepository, EventMapper eventMapper, EventDomainService eventDomainService) {
        return new RetrieverEventByIdUseCaseImpl(eventRepository, eventMapper, eventDomainService);
    }

    @Bean
    public RetrieverEventsByFilterUseCase retrieverEventsByFilterUseCase(EventRepository eventRepository, EventMapper eventMapper) {
        return new RetrieverEventsByFilterUseCaseImpl(eventRepository, eventMapper);
    }

    @Bean
    public RetrieverAllEventsUseCase retrieverAllEventsUseCase(EventRepository eventRepository, EventMapper eventMapper) {
        return new RetrieverAllEventsUseCaseImpl(eventRepository, eventMapper);
    }

    @Bean
    public CancellerEventUseCase cancelEventUseCase(EventRepository eventRepository, EventDomainService eventDomainService) {
        return new CancellerEventUseCaseImpl(eventRepository, eventDomainService);
    }
}