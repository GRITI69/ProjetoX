package com.projetox.monitoramento.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_monitoramento")
    private Monitoramento monitoramento;

    private String tipoAlerta;
    private String mensagem;
    private LocalDateTime dataHora = LocalDateTime.now();
    private boolean enviado = false;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Monitoramento getMonitoramento() { return monitoramento; }
    public void setMonitoramento(Monitoramento monitoramento) { this.monitoramento = monitoramento; }

    public String getTipoAlerta() { return tipoAlerta; }
    public void setTipoAlerta(String tipoAlerta) { this.tipoAlerta = tipoAlerta; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public boolean isEnviado() { return enviado; }
    public void setEnviado(boolean enviado) { this.enviado = enviado; }
}