package com.project.cleanarch.eventapi.infraestruct.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.cleanarch.eventapi.core.domain.enums.EventStatusEnum;
import com.project.cleanarch.eventapi.core.domain.enums.EventType;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para Event - Camada de Infraestrutura/Apresentação
 * Responsável pela transferência de dados entre as camadas externas
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventDTO {

    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "Nome do evento é obrigatório")
    @Size(min = 3, max = 255, message = "Nome deve ter entre 3 e 255 caracteres")
    @JsonProperty("name")
    private String name;

    @Size(max = 1000, message = "Descrição não pode ter mais de 1000 caracteres")
    @JsonProperty("description")
    private String description;

    @NotBlank(message = "Identificador é obrigatório")
    @Size(min = 3, max = 100, message = "Identificador deve ter entre 3 e 100 caracteres")
    @JsonProperty("identifier")
    private String identifier;

    @NotNull(message = "Data de início é obrigatória")
    @FutureOrPresent(message = "Data de início deve ser presente ou futura")
    @JsonProperty("startAt")
    private LocalDateTime startAt;

    @NotNull(message = "Data de fim é obrigatória")
    @FutureOrPresent(message = "Data de fim deve ser presente ou futura")
    @JsonProperty("endAt")
    private LocalDateTime endAt;

    @NotBlank(message = "Localização é obrigatória")
    @Size(min = 3, max = 255, message = "Localização deve ter entre 3 e 255 caracteres")
    @JsonProperty("location")
    private String location;

    @NotNull(message = "Capacidade é obrigatória")
    @Min(value = 1, message = "Capacidade deve ser pelo menos 1")
    @JsonProperty("capacity")
    private Integer capacity;

    @NotNull(message = "Tipo do evento é obrigatório")
    @JsonProperty("type")
    private EventType type;

    @JsonProperty("status")
    private EventStatusEnum status;

    @JsonProperty("cancelReason")
    private String cancelReason;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
}
