package com.projetox.monitoramento.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_maquina")
    private Maquina maquina;

    private String nomeServico;
    private String statusServico;
    private LocalDateTime dataVerificacao = LocalDateTime.now();

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Maquina getMaquina() { return maquina; }
    public void setMaquina(Maquina maquina) { this.maquina = maquina; }

    public String getNomeServico() { return nomeServico; }
    public void setNomeServico(String nomeServico) { this.nomeServico = nomeServico; }

    public String getStatusServico() { return statusServico; }
    public void setStatusServico(String statusServico) { this.statusServico = statusServico; }

    public LocalDateTime getDataVerificacao() { return dataVerificacao; }
    public void setDataVerificacao(LocalDateTime dataVerificacao) { this.dataVerificacao = dataVerificacao; }
}
