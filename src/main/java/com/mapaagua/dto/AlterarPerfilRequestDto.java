package com.mapaagua.dto;

import jakarta.validation.constraints.NotBlank;

public record AlterarPerfilRequestDto(
        @NotBlank(message = "Perfil é obrigatório")
        String perfil
) {
}