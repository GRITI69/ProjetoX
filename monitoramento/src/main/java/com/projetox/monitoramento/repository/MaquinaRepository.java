package com.projetox.monitoramento.repository;

import com.projetox.monitoramento.model.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaquinaRepository extends JpaRepository<Maquina, Long> {
    Maquina findByNome(String nome);
}
