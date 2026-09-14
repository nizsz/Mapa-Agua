package com.mapaagua.dto;

import com.mapaagua.enums.StatusSolicitacao;
import com.mapaagua.enums.UrgenciaSolicitacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SolicitacaoResponseDto(
        Long id,
        Long usuarioId,
        Long distribuidorId,
        Integer quantidadeLitros,
        Integer quantidadePessoas,
        UrgenciaSolicitacao urgencia,
        String descricao,
        String endereco,
        BigDecimal latitude,
        BigDecimal longitude,
        StatusSolicitacao status,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {
}
