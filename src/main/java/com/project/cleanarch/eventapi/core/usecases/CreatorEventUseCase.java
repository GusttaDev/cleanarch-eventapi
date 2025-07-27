package com.project.cleanarch.eventapi.core.usecases;

import com.project.cleanarch.eventapi.core.domain.model.Event;

public interface CreatorEventUseCase {

    Event execute(Event event);
}
