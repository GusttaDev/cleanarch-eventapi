package com.project.cleanarch.eventapi.infraestruct.presentation.handler;

import java.util.List;

import lombok.Builder;

@Builder
public record  ErrorMessage(
        Integer status,
        String timestamp,
        String code,
        String message,
        List<ErrorMessage> details
) {
}
