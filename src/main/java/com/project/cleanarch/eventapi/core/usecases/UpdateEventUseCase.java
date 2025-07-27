package com.project.cleanarch.eventapi.core.usecases;

import com.project.cleanarch.eventapi.core.domain.model.Event;

public interface UpdateEventUseCase {

    Event execute(Long id, Event event);
} 