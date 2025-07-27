package com.project.cleanarch.eventapi.core.usecases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.project.cleanarch.eventapi.core.domain.model.Event;

public interface RetrieverAllEventsUseCase {
    Page<Event> execute(Pageable pageable);
}