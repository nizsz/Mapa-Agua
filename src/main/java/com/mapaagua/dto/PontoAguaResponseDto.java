package com.mapaagua.dto;

import com.mapaagua.enums.DisponibilidadeAgua;
import com.mapaagua.enums.StatusAprovacaoPonto;
import com.mapaagua.enums.TipoPontoAgua;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record PontoAguaResponseDto(
        Long id,
        String nome,
        String descricao,
        TipoPontoAgua tipo,
        String endereco,
        Double latitude,
        Double longitude,
        LocalTime horarioInicio,
        LocalTime horarioFim,
        DisponibilidadeAgua disponibilidade,
        String observacao,
        Long usuarioId,
        StatusAprovacaoPonto statusAprovacao,
        LocalDateTime dataCadastro
) {
}