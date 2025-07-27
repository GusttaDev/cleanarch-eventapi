package com.project.cleanarch.eventapi.core.domain.service;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import com.project.cleanarch.eventapi.core.domain.enums.EventStatusEnum;
import com.project.cleanarch.eventapi.core.domain.enums.EventType;
import com.project.cleanarch.eventapi.core.domain.exception.BusinessException;
import com.project.cleanarch.eventapi.core.domain.model.Event;
import com.project.cleanarch.eventapi.core.gateway.EventRepository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
@DisplayName("EventDomainService")
class EventDomainServiceTest {

    @Mock
    private EventRepository eventRepository;

    private EventDomainService eventDomainService;

    @BeforeEach
    void setUp() {
        eventDomainService = new EventDomainService(eventRepository);
    }

    @Test
    @DisplayName("Deve validar criação de evento com sucesso")
    void shouldValidateEventCreationSuccessfully() {
        Event event = createValidEvent();
        when(eventRepository.existsByIdentifier("test-event")).thenReturn(false);

        assertDoesNotThrow(() -> eventDomainService.validateEventCreation(event));
        verify(eventRepository).existsByIdentifier("test-event");
    }

    @Test
    @DisplayName("Deve falhar quando identifier já existe")
    void shouldFailWhenIdentifierAlreadyExists() {
        Event event = createValidEvent();
        when(eventRepository.existsByIdentifier("test-event")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> eventDomainService.validateEventCreation(event));

        assertEquals("event.already.exists", exception.getCode());
    }

    @Test
    @DisplayName("Deve falhar quando data de início é posterior à data de fim")
    void shouldFailWhenStartDateIsAfterEndDate() {
        Event event = new Event(
                "Test Event",
                "Description",
                "test-event",
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(1),
                "Location",
                100,
                EventType.CONFERENCE,
                EventStatusEnum.OPEN,
                null
        );
        when(eventRepository.existsByIdentifier("test-event")).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> eventDomainService.validateEventCreation(event));

        assertEquals("event.invalid.date.range", exception.getCode());
    }

    @Test
    @DisplayName("Deve falhar quando capacidade é inválida")
    void shouldFailWhenCapacityIsInvalid() {
        Event event = new Event(
                "Test Event",
                "Description",
                "test-event",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "Location",
                0,
                EventType.CONFERENCE,
                EventStatusEnum.DRAFT,
                null
        );
        when(eventRepository.existsByIdentifier("test-event")).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> eventDomainService.validateEventCreation(event));

        assertEquals("event.invalid.capacity", exception.getCode());
    }

    @Test
    @DisplayName("Deve validar ID com sucesso")
    void shouldValidateIdSuccessfully() {
        assertDoesNotThrow(() -> eventDomainService.validateId(1L));
    }

    @Test
    @DisplayName("Deve falhar quando ID é nulo")
    void shouldFailWhenIdIsNull() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> eventDomainService.validateId(null));

        assertEquals("event.id.invalid", exception.getCode());
    }

    @Test
    @DisplayName("Deve falhar quando ID é zero")
    void shouldFailWhenIdIsZero() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> eventDomainService.validateId(0L));

        assertEquals("event.id.invalid", exception.getCode());
    }

    @Test
    @DisplayName("Deve validar cancelamento com sucesso")
    void shouldValidateCancellationSuccessfully() {
        assertDoesNotThrow(() -> eventDomainService.validateEventCancellation(1L, "Test reason"));
    }

    @Test
    @DisplayName("Deve falhar quando motivo do cancelamento é nulo")
    void shouldFailWhenCancelReasonIsNull() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> eventDomainService.validateEventCancellation(1L, null));

        assertEquals("event.cancel.reason.required", exception.getCode());
    }

    @Test
    @DisplayName("Deve falhar quando motivo do cancelamento está vazio")
    void shouldFailWhenCancelReasonIsEmpty() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> eventDomainService.validateEventCancellation(1L, ""));

        assertEquals("event.cancel.reason.required", exception.getCode());
    }

    private Event createValidEvent() {
        return new Event(
                "Test Event",
                "Description",
                "test-event",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "Location",
                100,
                EventType.CONFERENCE,
                EventStatusEnum.COMPLETED,
                null
        );
    }
} 