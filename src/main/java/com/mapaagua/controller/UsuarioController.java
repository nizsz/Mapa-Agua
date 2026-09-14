package com.mapaagua.controller;

import com.mapaagua.dto.UsuarioCadastroRequestDto;
import com.mapaagua.dto.UsuarioResponseDto;
import com.mapaagua.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> criar(
            @Valid @RequestBody UsuarioCadastroRequestDto requestDto) {

        UsuarioResponseDto responseDto = usuarioService.criar(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
