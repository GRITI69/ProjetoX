package com.projetox.monitoramento.repository;

import com.projetox.monitoramento.model.Monitoramento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonitoramentoRepository extends JpaRepository<Monitoramento, Long> {
    List<Monitoramento> findTop10ByOrderByDataHoraDesc();
}
