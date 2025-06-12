package com.projetox.monitoramento.controller;

import com.projetox.monitoramento.dto.MonitoramentoDTO;
import com.projetox.monitoramento.model.Maquina;
import com.projetox.monitoramento.model.Monitoramento;
import com.projetox.monitoramento.repository.MaquinaRepository;
import com.projetox.monitoramento.service.MonitoramentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitoramentos")
@CrossOrigin
public class MonitoramentoController {

    @Autowired
    private MonitoramentoService monitoramentoService;

    @Autowired
    private MaquinaRepository maquinaRepository;

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody MonitoramentoDTO dto) {
        Maquina maquina = maquinaRepository.findByNome(dto.getMaquina());

        if (maquina == null) {
            maquina = new Maquina();
            maquina.setNome(dto.getMaquina());
            maquina = maquinaRepository.save(maquina);
        }

        Monitoramento monitoramento = new Monitoramento();
        monitoramento.setCpuUsage(dto.getCpu());
        monitoramento.setRamUsage(dto.getRam());
        monitoramento.setDiskUsage(dto.getDisco());
        monitoramento.setTemperatura(dto.getTemperatura());
        monitoramento.setMaquina(maquina);

        return ResponseEntity.status(HttpStatus.CREATED).body(monitoramentoService.salvar(monitoramento));
    }

    @GetMapping
    public List<Monitoramento> listarTodos() {
        return monitoramentoService.listarTodos();
    }
}
