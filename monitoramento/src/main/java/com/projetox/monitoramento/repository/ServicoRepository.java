package com.projetox.monitoramento.repository;

import com.projetox.monitoramento.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
}
