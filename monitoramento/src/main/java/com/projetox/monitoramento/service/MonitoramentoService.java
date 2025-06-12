package com.projetox.monitoramento.service;

import com.projetox.monitoramento.model.Alerta;
import com.projetox.monitoramento.model.Monitoramento;
import com.projetox.monitoramento.repository.AlertaRepository;
import com.projetox.monitoramento.repository.MonitoramentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitoramentoService {

    @Autowired
    private MonitoramentoRepository monitoramentoRepository;

    @Autowired
    private AlertaRepository alertaRepository;

    public Monitoramento salvar(Monitoramento monitoramento) {
        // Salva o monitoramento
        Monitoramento salvo = monitoramentoRepository.save(monitoramento);

        // Verifica e gera alertas
        if (salvo.getTemperatura() != null && salvo.getTemperatura() > 75.0) {
            criarAlerta(salvo, "Temperatura", "Temperatura crítica: " + salvo.getTemperatura() + "°C");
        }

        if (salvo.getCpuUsage() != null && salvo.getCpuUsage() > 90.0) {
            criarAlerta(salvo, "CPU", "Uso de CPU elevado: " + salvo.getCpuUsage() + "%");
        }

        if (salvo.getRamUsage() != null && salvo.getRamUsage() > 90.0) {
            criarAlerta(salvo, "RAM", "Uso de RAM elevado: " + salvo.getRamUsage() + "%");
        }

        if (salvo.getDiskUsage() != null && salvo.getDiskUsage() > 90.0) {
            criarAlerta(salvo, "Disco", "Uso de disco elevado: " + salvo.getDiskUsage() + "%");
        }

        return salvo;
    }

    private void criarAlerta(Monitoramento monitoramento, String tipo, String mensagem) {
        Alerta alerta = new Alerta();
        alerta.setMonitoramento(monitoramento);
        alerta.setTipoAlerta(tipo);
        alerta.setMensagem(mensagem);
        alerta.setEnviado(false); // ou true se for enviado por e-mail etc.
        alertaRepository.save(alerta);

        System.out.println("🚨 Alerta gerado: " + tipo + " - " + mensagem);
    }

    public List<Monitoramento> listarTodos() {
        return monitoramentoRepository.findAll();
    }
}
