package com.mapaagua.repository;

import com.mapaagua.entity.PontoAgua;
import com.mapaagua.enums.DisponibilidadeAgua;
import com.mapaagua.enums.StatusAprovacaoPonto;
import com.mapaagua.enums.TipoPontoAgua;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PontoAguaRepository extends JpaRepository<PontoAgua, Long> {

    List<PontoAgua> findByTipo(TipoPontoAgua tipo);

    List<PontoAgua> findByDisponibilidade(DisponibilidadeAgua disponibilidade);

    List<PontoAgua> findByStatusAprovacao(StatusAprovacaoPonto statusAprovacao);

    List<PontoAgua> findByUsuarioId(Long usuarioId);
}