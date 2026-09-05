package com.mapaagua.dto;

import com.mapaagua.enums.DisponibilidadeAgua;
import com.mapaagua.enums.TipoPontoAgua;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record PontoAguaRequestDto(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        String descricao,

        @NotNull(message = "Tipo é obrigatório")
        TipoPontoAgua tipo,

        @NotBlank(message = "Endereço é obrigatório")
        String endereco,

        @NotNull(message = "Latitude é obrigatória")
        @DecimalMin(value = "-90.0", message = "Latitude fora do intervalo válido")
        @DecimalMax(value = "90.0", message = "Latitude fora do intervalo válido")
        Double latitude,

        @NotNull(message = "Longitude é obrigatória")
        @DecimalMin(value = "-180.0", message = "Longitude fora do intervalo válido")
        @DecimalMax(value = "180.0", message = "Longitude fora do intervalo válido")
        Double longitude,

        @NotNull(message = "Horário de início é obrigatório")
        LocalTime horarioInicio,

        @NotNull(message = "Horário de fim é obrigatório")
        LocalTime horarioFim,

        @NotNull(message = "Disponibilidade é obrigatória")
        DisponibilidadeAgua disponibilidade,

        String observacao
) {
}