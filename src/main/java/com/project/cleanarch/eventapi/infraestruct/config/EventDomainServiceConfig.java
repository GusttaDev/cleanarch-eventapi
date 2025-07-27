package com.project.cleanarch.eventapi.infraestruct.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.cleanarch.eventapi.core.domain.service.EventDomainService;
import com.project.cleanarch.eventapi.core.gateway.EventRepository;

@Configuration
public class EventDomainServiceConfig {

    @Bean
    public EventDomainService eventDomainService(EventRepository eventRepository) {
        return new EventDomainService(eventRepository);
    }
} 