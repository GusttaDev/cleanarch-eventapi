package com.project.cleanarch.eventapi.core.usecases;

public interface CancellerEventUseCase {
    void execute(Long id, String reason);
} 