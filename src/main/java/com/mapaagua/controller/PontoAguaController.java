package com.mapaagua.controller;

import com.mapaagua.dto.PontoAguaRequestDto;
import com.mapaagua.dto.PontoAguaResponseDto;
import com.mapaagua.service.PontoAguaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PontoAguaController {

    private final PontoAguaService pontoAguaService;

    public PontoAguaController(PontoAguaService pontoAguaService) {
        this.pontoAguaService = pontoAguaService;
    }

    @GetMapping("/pontos")
    public ResponseEntity<List<PontoAguaResponseDto>> listarTodos() {
        return ResponseEntity.ok(pontoAguaService.listarTodos());
    }

    @GetMapping("/pontos/{id}")
    public ResponseEntity<PontoAguaResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pontoAguaService.buscarPorId(id));
    }

    @PostMapping("/pontos")
    public ResponseEntity<PontoAguaResponseDto> criar(@Valid @RequestBody PontoAguaRequestDto requestDto) {
        PontoAguaResponseDto responseDto = pontoAguaService.criar(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/pontos/{id}")
    public ResponseEntity<PontoAguaResponseDto> atualizar(@PathVariable Long id,
                                                           @Valid @RequestBody PontoAguaRequestDto requestDto) {
        return ResponseEntity.ok(pontoAguaService.atualizar(id, requestDto));
    }

    @DeleteMapping("/pontos/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pontoAguaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}