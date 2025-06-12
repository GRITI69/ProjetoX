package com.projetox.monitoramento.service;

import com.projetox.monitoramento.model.Maquina;
import com.projetox.monitoramento.repository.MaquinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaquinaService {

    @Autowired
    private MaquinaRepository maquinaRepository;

    public List<Maquina> listarTodas() {
        return maquinaRepository.findAll();
    }

    public Maquina salvar(Maquina maquina) {
        return maquinaRepository.save(maquina);
    }
}