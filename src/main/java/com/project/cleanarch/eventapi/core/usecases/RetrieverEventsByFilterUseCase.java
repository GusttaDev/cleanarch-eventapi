package com.project.cleanarch.eventapi.core.usecases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.project.cleanarch.eventapi.core.domain.model.Event;
import com.project.cleanarch.eventapi.infraestruct.dtos.EventFilterDTO;

public interface RetrieverEventsByFilterUseCase {
    Page<Event> execute(EventFilterDTO filterDTO, Pageable pageable);
}