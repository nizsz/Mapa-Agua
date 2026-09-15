package com.mapaagua.controller;

import com.mapaagua.dto.AlterarPerfilRequestDto;
import com.mapaagua.dto.UsuarioCadastroRequestDto;
import com.mapaagua.dto.UsuarioResponseDto;
import com.mapaagua.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @PatchMapping("/{id}/perfil")
    public ResponseEntity<UsuarioResponseDto> alterarPerfil(
            @PathVariable Long id,
            @Valid @RequestBody AlterarPerfilRequestDto requestDto) {

        UsuarioResponseDto responseDto = usuarioService.alterarPerfil(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listar() {
        List<UsuarioResponseDto> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }
}