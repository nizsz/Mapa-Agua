package com.mapaagua.controller;

import com.mapaagua.dto.SolicitacaoRequestDto;
import com.mapaagua.dto.SolicitacaoResponseDto;
import com.mapaagua.service.SolicitacaoService;
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
@RequestMapping("/api/solicitacoes")
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    public SolicitacaoController(SolicitacaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoResponseDto>> listar() {
        return ResponseEntity.ok(solicitacaoService.listar());
    }

    @PatchMapping("/{id}/iniciar-analise")
    public ResponseEntity<SolicitacaoResponseDto> iniciarAnalise(@PathVariable Long id) {
        return ResponseEntity.ok(solicitacaoService.iniciarAnalise(id));
    }

    @PostMapping
    public ResponseEntity<SolicitacaoResponseDto> criar(
            @Valid @RequestBody SolicitacaoRequestDto request) {

        SolicitacaoResponseDto responseDto = solicitacaoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/{id}/assumir")
    public ResponseEntity<SolicitacaoResponseDto> assumir(@PathVariable Long id) {
        return ResponseEntity.ok(solicitacaoService.assumir(id));
    }

    @PatchMapping("/{id}/iniciar-distribuicao")
    public ResponseEntity<SolicitacaoResponseDto> iniciarDistribuicao(@PathVariable Long id) {
        return ResponseEntity.ok(solicitacaoService.iniciarDistribuicao(id));
    }

    @PatchMapping("/{id}/atender")
    public ResponseEntity<SolicitacaoResponseDto> atender(@PathVariable Long id) {
        return ResponseEntity.ok(solicitacaoService.atender(id));
    }
}
