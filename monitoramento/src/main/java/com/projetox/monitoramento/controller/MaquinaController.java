package com.projetox.monitoramento.controller;

import com.projetox.monitoramento.model.Maquina;
import com.projetox.monitoramento.service.MaquinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maquinas")
@CrossOrigin
public class MaquinaController {

    @Autowired
    private MaquinaService maquinaService;

    @GetMapping
    public List<Maquina> listarTodas() {
        return maquinaService.listarTodas();
    }

    @PostMapping
    public Maquina salvar(@RequestBody Maquina maquina) {
        return maquinaService.salvar(maquina);
    }
}
