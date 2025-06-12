package com.projetox.monitoramento.service;

import com.projetox.monitoramento.model.Alerta;
import com.projetox.monitoramento.repository.AlertaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertaService {

    @Autowired
    private AlertaRepository alertaRepository;

    public Alerta salvar(Alerta alerta) {
        return alertaRepository.save(alerta);
    }

    public List<Alerta> listarTodos() {
        return alertaRepository.findAll();
    }
}