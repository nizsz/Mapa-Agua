package com.mapaagua.service;

import com.mapaagua.dto.SolicitacaoRequestDto;
import com.mapaagua.dto.SolicitacaoResponseDto;
import com.mapaagua.entity.Solicitacao;
import com.mapaagua.entity.Usuario;
import com.mapaagua.enums.PerfilUsuario;
import com.mapaagua.enums.StatusSolicitacao;
import com.mapaagua.repository.SolicitacaoRepository;
import com.mapaagua.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final UsuarioRepository usuarioRepository;

    public SolicitacaoService(SolicitacaoRepository solicitacaoRepository,
                              UsuarioRepository usuarioRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public SolicitacaoResponseDto criar(SolicitacaoRequestDto request) {
        Usuario usuario = obterUsuarioAutenticado();
        if (usuario.getPerfil() != PerfilUsuario.MORADOR
                && usuario.getPerfil() != PerfilUsuario.ADMINISTRADOR) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Usuário não possui permissão para criar solicitações");
        }

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setUsuario(usuario);
        solicitacao.setDistribuidor(null);
        solicitacao.setQuantidadeLitros(request.quantidadeLitros());
        solicitacao.setQuantidadePessoas(request.quantidadePessoas());
        solicitacao.setUrgencia(request.urgencia());
        solicitacao.setDescricao(request.descricao());
        solicitacao.setEndereco(request.endereco());
        solicitacao.setLatitude(request.latitude());
        solicitacao.setLongitude(request.longitude());
        solicitacao.setStatus(StatusSolicitacao.CRIADA);

        return toResponseDto(solicitacaoRepository.save(solicitacao));
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoResponseDto> listar() {
        Usuario usuario = obterUsuarioAutenticado();
        List<Solicitacao> solicitacoes;

        if (usuario.getPerfil() == PerfilUsuario.ADMINISTRADOR) {
            solicitacoes = solicitacaoRepository.findAll();
        } else if (usuario.getPerfil() == PerfilUsuario.MORADOR) {
            solicitacoes = solicitacaoRepository.findByUsuarioId(usuario.getId());
        } else if (usuario.getPerfil() == PerfilUsuario.DISTRIBUIDOR) {
            solicitacoes = new ArrayList<>(solicitacaoRepository.findByDistribuidorId(usuario.getId()));
            solicitacoes.addAll(solicitacaoRepository.findAll().stream()
                    .filter(solicitacao -> solicitacao.getDistribuidor() == null)
                    .toList());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Usuário não possui permissão para consultar solicitações");
        }

        return solicitacoes.stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional
    public SolicitacaoResponseDto assumir(Long id) {
        Usuario usuario = obterUsuarioAutenticado();
        if (usuario.getPerfil() != PerfilUsuario.DISTRIBUIDOR) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Somente distribuidores podem assumir solicitações");
        }

        Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Solicitação não encontrada"));

        if (solicitacao.getDistribuidor() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Solicitação já possui distribuidor");
        }

        if (solicitacao.getStatus() != StatusSolicitacao.CRIADA) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Solicitação não está disponível para aceite");
        }

        solicitacao.setDistribuidor(usuario);
        solicitacao.setStatus(StatusSolicitacao.ACEITA);

        return toResponseDto(solicitacaoRepository.save(solicitacao));
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
                    "Usuário inativo não pode criar solicitações");
        }

        return usuario;
    }

    private SolicitacaoResponseDto toResponseDto(Solicitacao solicitacao) {
        return new SolicitacaoResponseDto(
                solicitacao.getId(),
                solicitacao.getUsuario().getId(),
                solicitacao.getDistribuidor() == null ? null : solicitacao.getDistribuidor().getId(),
                solicitacao.getQuantidadeLitros(),
                solicitacao.getQuantidadePessoas(),
                solicitacao.getUrgencia(),
                solicitacao.getDescricao(),
                solicitacao.getEndereco(),
                solicitacao.getLatitude(),
                solicitacao.getLongitude(),
                solicitacao.getStatus(),
                solicitacao.getDataCriacao(),
                solicitacao.getDataAtualizacao()
        );
    }
}
