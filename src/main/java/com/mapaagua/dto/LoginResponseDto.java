package com.mapaagua.dto;

import com.mapaagua.enums.PerfilUsuario;

public record LoginResponseDto(
        Long id,
        String nome,
        String email,
        PerfilUsuario perfil
) {
}
