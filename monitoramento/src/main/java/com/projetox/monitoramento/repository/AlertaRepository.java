package com.projetox.monitoramento.repository;

import com.projetox.monitoramento.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
}