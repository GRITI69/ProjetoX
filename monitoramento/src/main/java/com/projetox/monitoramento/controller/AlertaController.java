package com.projetox.monitoramento.controller;

import com.projetox.monitoramento.model.Alerta;
import com.projetox.monitoramento.service.AlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@CrossOrigin
public class AlertaController {

    @Autowired
    private AlertaService alertaService;

    @GetMapping
    public List<Alerta> listarTodos() {
        return alertaService.listarTodos();
    }

    @PostMapping
    public Alerta salvar(@RequestBody Alerta alerta) {
        return alertaService.salvar(alerta);
    }
}
