package com.mapaagua.service;

import com.mapaagua.dto.PontoAguaRequestDto;
import com.mapaagua.dto.PontoAguaResponseDto;
import com.mapaagua.entity.PontoAgua;
import com.mapaagua.entity.Usuario;
import com.mapaagua.enums.StatusAprovacaoPonto;
import com.mapaagua.repository.PontoAguaRepository;
import com.mapaagua.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class PontoAguaService {

    private static final String PONTO_NAO_ENCONTRADO = "Ponto de água não encontrado";

    private final PontoAguaRepository pontoAguaRepository;
    private final UsuarioRepository usuarioRepository;

    public PontoAguaService(PontoAguaRepository pontoAguaRepository,
                            UsuarioRepository usuarioRepository) {
        this.pontoAguaRepository = pontoAguaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<PontoAguaResponseDto> listarTodos() {
        return pontoAguaRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PontoAguaResponseDto buscarPorId(Long id) {
        return toResponseDto(buscarEntidadePorId(id));
    }

    @Transactional
    public PontoAguaResponseDto criar(PontoAguaRequestDto requestDto) {
        validarHorarios(requestDto.horarioInicio(), requestDto.horarioFim());

        PontoAgua pontoAgua = new PontoAgua();
        aplicarDados(pontoAgua, requestDto);
        pontoAgua.setUsuario(obterUsuarioAutenticado());
        pontoAgua.setStatusAprovacao(StatusAprovacaoPonto.PENDENTE);
        pontoAgua.setDataCadastro(LocalDateTime.now());

        return toResponseDto(pontoAguaRepository.save(pontoAgua));
    }

    @Transactional
    public PontoAguaResponseDto atualizar(Long id, PontoAguaRequestDto requestDto) {
        PontoAgua pontoAgua = buscarEntidadePorId(id);
        validarHorarios(requestDto.horarioInicio(), requestDto.horarioFim());
        aplicarDados(pontoAgua, requestDto);

        return toResponseDto(pontoAguaRepository.save(pontoAgua));
    }

    @Transactional
    public PontoAguaResponseDto atualizarStatus(Long id, StatusAprovacaoPonto novoStatus) {
        PontoAgua pontoAgua = buscarEntidadePorId(id);
        StatusAprovacaoPonto statusAtual = pontoAgua.getStatusAprovacao();

        if (novoStatus == null
                || statusAtual != StatusAprovacaoPonto.PENDENTE
                || (novoStatus != StatusAprovacaoPonto.APROVADO
                && novoStatus != StatusAprovacaoPonto.REJEITADO)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Transição de status não permitida: " + statusAtual + " para " + novoStatus
            );
        }

        pontoAgua.setStatusAprovacao(novoStatus);
        return toResponseDto(pontoAguaRepository.save(pontoAgua));
    }

    @Transactional
    public void deletar(Long id) {
        if (!pontoAguaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, PONTO_NAO_ENCONTRADO);
        }
        pontoAguaRepository.deleteById(id);
    }

    private PontoAgua buscarEntidadePorId(Long id) {
        return pontoAguaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, PONTO_NAO_ENCONTRADO));
    }

    private Usuario obterUsuarioAutenticado() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuário não autenticado");
        }

        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário autenticado não encontrado"));

        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Usuário inativo não pode criar pontos de água");
        }

        return usuario;
    }

    private void aplicarDados(PontoAgua pontoAgua, PontoAguaRequestDto requestDto) {
        pontoAgua.setNome(requestDto.nome());
        pontoAgua.setDescricao(requestDto.descricao());
        pontoAgua.setTipo(requestDto.tipo());
        pontoAgua.setEndereco(requestDto.endereco());
        pontoAgua.setLatitude(requestDto.latitude());
        pontoAgua.setLongitude(requestDto.longitude());
        pontoAgua.setHorarioInicio(requestDto.horarioInicio());
        pontoAgua.setHorarioFim(requestDto.horarioFim());
        pontoAgua.setDisponibilidade(requestDto.disponibilidade());
        pontoAgua.setObservacao(requestDto.observacao());
    }

    private void validarHorarios(LocalTime horarioInicio, LocalTime horarioFim) {
        if (horarioFim.isBefore(horarioInicio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Horário final não pode ser anterior ao horário inicial");
        }
    }

    private PontoAguaResponseDto toResponseDto(PontoAgua pontoAgua) {
        return new PontoAguaResponseDto(
                pontoAgua.getId(),
                pontoAgua.getNome(),
                pontoAgua.getDescricao(),
                pontoAgua.getTipo(),
                pontoAgua.getEndereco(),
                pontoAgua.getLatitude(),
                pontoAgua.getLongitude(),
                pontoAgua.getHorarioInicio(),
                pontoAgua.getHorarioFim(),
                pontoAgua.getDisponibilidade(),
                pontoAgua.getObservacao(),
                pontoAgua.getUsuario().getId(),
                pontoAgua.getStatusAprovacao(),
                pontoAgua.getDataCadastro()
        );
    }
}