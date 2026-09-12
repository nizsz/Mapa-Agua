package com.mapaagua.dto;

import com.mapaagua.enums.StatusAprovacaoPonto;
import jakarta.validation.constraints.NotNull;

public record StatusPontoRequestDto(

        @NotNull(message = "Status de aprovação é obrigatório")
        StatusAprovacaoPonto statusAprovacao
) {
}
