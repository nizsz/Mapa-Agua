package com.mapaagua.repository;

import com.mapaagua.entity.Solicitacao;
import com.mapaagua.enums.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

    List<Solicitacao> findByUsuarioId(Long usuarioId);

    List<Solicitacao> findByDistribuidorId(Long distribuidorId);

    List<Solicitacao> findByStatus(StatusSolicitacao status);
}
