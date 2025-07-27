package com.project.cleanarch.eventapi.core.domain.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final String code;
    private final transient Object[] args;

    public BusinessException(String codeMessage, Object... args) {
        super(codeMessage);
        this.code = codeMessage;
        this.args = args;
    }
}