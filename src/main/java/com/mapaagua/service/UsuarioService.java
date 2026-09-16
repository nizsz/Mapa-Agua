package com.mapaagua.service;

import com.mapaagua.dto.AlterarPerfilRequestDto;
import com.mapaagua.dto.UsuarioCadastroRequestDto;
import com.mapaagua.dto.UsuarioResponseDto;
import com.mapaagua.entity.Usuario;
import com.mapaagua.enums.PerfilUsuario;
import com.mapaagua.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
public class UsuarioService {

    private static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado";
    private static final List<String> DOMINIOS_EMAIL_PERMITIDOS = List.of("gmail.com", "hotmail.com", "outlook.com");

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDto criar(UsuarioCadastroRequestDto requestDto) {
        String email = normalizarEmail(requestDto.email());
        validarDominioEmail(email);

        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(requestDto.nome().trim());
        usuario.setEmail(email);
        usuario.setSenhaHash(passwordEncoder.encode(requestDto.senha()));
        usuario.setPerfil(PerfilUsuario.MORADOR);
        usuario.setAtivo(true);

        return toResponseDto(usuarioRepository.save(usuario));
    }

    public UsuarioResponseDto buscarPorId(Long id) {
        return toResponseDto(buscarEntidadePorId(id));
    }

    public List<UsuarioResponseDto> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional
    public UsuarioResponseDto alterarPerfil(Long id, AlterarPerfilRequestDto requestDto) {
        Usuario usuario = buscarEntidadePorId(id);
        usuario.setPerfil(parsePerfilAlteravel(requestDto.perfil()));

        return toResponseDto(usuarioRepository.save(usuario));
    }

    private PerfilUsuario parsePerfilAlteravel(String perfil) {
        PerfilUsuario perfilUsuario;
        try {
            perfilUsuario = PerfilUsuario.valueOf(perfil.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Perfil inválido");
        }

        if (perfilUsuario == PerfilUsuario.ADMINISTRADOR) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não é permitido definir o perfil ADMINISTRADOR por esta operação");
        }

        return perfilUsuario;
    }

    private Usuario buscarEntidadePorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USUARIO_NAO_ENCONTRADO));
    }

    private Usuario buscarEntidadePorEmail(String email) {
        return usuarioRepository.findByEmailIgnoreCase(normalizarEmail(email))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USUARIO_NAO_ENCONTRADO));
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private void validarDominioEmail(String email) {
        String dominio = email.substring(email.indexOf('@') + 1);
        if (!DOMINIOS_EMAIL_PERMITIDOS.contains(dominio)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Use um email Gmail, Hotmail ou Outlook.");
        }
    }

    private UsuarioResponseDto toResponseDto(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.getAtivo(),
                usuario.getDataCriacao(),
                usuario.getDataAtualizacao()
        );
    }
}