package com.project.cleanarch.eventapi.infraestruct.presentation.handler;

import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.project.cleanarch.eventapi.core.domain.exception.BusinessException;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpServerErrorException.class)
    ErrorMessage handlerHttpServerErrorException(final HttpServerErrorException exception){
        log.error("m=handlerHttpServerErrorException error={}", exception.getMessage());
        return buildErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.builder()
                .code("error.internal-server")
                .message(getMessage("error.internal-server")));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpClientErrorException.class)
    ErrorMessage handlerHttpClientErrorException(final HttpClientErrorException exception){
        log.error("m=handlerHttpClientErrorException error={}", exception.getMessage());

        return buildErrorMessage(HttpStatus.BAD_REQUEST, ErrorMessage.builder()
                .code("invalid.bad-request")
                .message(getMessage("invalid.bad-request")));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    ErrorMessage handlerMissingRequestHeaderException(final MissingRequestHeaderException exception){
        log.error("m=handlerMissingRequestHeaderException error={}", exception.getMessage());

        return buildErrorMessage(HttpStatus.BAD_REQUEST, ErrorMessage.builder()
                .code("invalid.http-header")
                .message(exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BusinessException.class)
    ErrorMessage handlerBusinessException(final BusinessException exception){
        log.error("m=handlerBusinessException error={}", exception.getMessage());
        return buildErrorMessage(HttpStatus.UNPROCESSABLE_ENTITY,
                ErrorMessage.builder().code(exception.getCode())
                                      .message(exception.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("m=handleMethodArgumentNotValid error={}", ex.getMessage());

        var details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> ErrorMessage.builder()
                        .code(error.getField())
                        .message(error.getDefaultMessage())
                        .build())
                .toList();

        var errorResponse = ErrorMessage.builder()
                .code("invalid.fields")
                .message(getMessage("{invalid.fields}"))
                .details(details);


        return buildResponseEntityErrorMessage(HttpStatus.BAD_REQUEST, errorResponse);
    }

    private ResponseEntity<Object> buildResponseEntityErrorMessage(@NotNull final HttpStatus httpStatus,
                                                                   @NotNull final ErrorMessage.ErrorMessageBuilder builder) {
        return ResponseEntity.status(httpStatus).body(buildErrorMessage(httpStatus, builder));
    }

    private ErrorMessage buildErrorMessage(HttpStatus httpStatus, ErrorMessage.ErrorMessageBuilder builder) {
        return builder.timestamp(LocalDateTime.now().toString())
                .status(httpStatus.value())
                .build();
    }

    private String getMessage(String code, @Nullable Object... args){
        try{
            return messageSource.getMessage(code, args, Locale.getDefault());
        }catch (final Exception exception){
            return code;
        }

    }
}
