package com.mapaagua.controller;

import com.mapaagua.dto.SolicitacaoRequestDto;
import com.mapaagua.dto.SolicitacaoResponseDto;
import com.mapaagua.service.SolicitacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    public SolicitacaoController(SolicitacaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @PostMapping
    public ResponseEntity<SolicitacaoResponseDto> criar(
            @Valid @RequestBody SolicitacaoRequestDto request) {

        SolicitacaoResponseDto responseDto = solicitacaoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
