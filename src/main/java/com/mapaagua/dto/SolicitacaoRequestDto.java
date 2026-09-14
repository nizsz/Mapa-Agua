package com.mapaagua.dto;

import com.mapaagua.enums.UrgenciaSolicitacao;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record SolicitacaoRequestDto(

        @NotNull(message = "Quantidade de litros é obrigatória")
        @Positive(message = "Quantidade de litros deve ser positiva")
        Integer quantidadeLitros,

        @NotNull(message = "Quantidade de pessoas é obrigatória")
        @Positive(message = "Quantidade de pessoas deve ser positiva")
        Integer quantidadePessoas,

        @NotNull(message = "Urgência é obrigatória")
        UrgenciaSolicitacao urgencia,

        String descricao,

        @NotBlank(message = "Endereço é obrigatório")
        String endereco,

        @NotNull(message = "Latitude é obrigatória")
        @DecimalMin(value = "-90.0", message = "Latitude fora do intervalo válido")
        @DecimalMax(value = "90.0", message = "Latitude fora do intervalo válido")
        BigDecimal latitude,

        @NotNull(message = "Longitude é obrigatória")
        @DecimalMin(value = "-180.0", message = "Longitude fora do intervalo válido")
        @DecimalMax(value = "180.0", message = "Longitude fora do intervalo válido")
        BigDecimal longitude
) {
}
