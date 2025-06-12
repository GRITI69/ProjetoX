package com.projetox.monitoramento.controller;

import com.projetox.monitoramento.model.Servico;
import com.projetox.monitoramento.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
@CrossOrigin
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @PostMapping
    public Servico salvar(@RequestBody Servico servico) {
        return servicoService.salvar(servico);
    }

    @GetMapping
    public List<Servico> listarTodos() {
        return servicoService.listarTodos();
    }
}