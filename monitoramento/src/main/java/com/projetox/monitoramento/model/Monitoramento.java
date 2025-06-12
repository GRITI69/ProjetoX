package com.projetox.monitoramento.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Monitoramento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_maquina")
    private Maquina maquina;

    private Double cpuUsage;
    private Double ramUsage;
    private Double diskUsage;
    private Double temperatura;
    private LocalDateTime dataHora = LocalDateTime.now();

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Maquina getMaquina() { return maquina; }
    public void setMaquina(Maquina maquina) { this.maquina = maquina; }

    public Double getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(Double cpuUsage) { this.cpuUsage = cpuUsage; }

    public Double getRamUsage() { return ramUsage; }
    public void setRamUsage(Double ramUsage) { this.ramUsage = ramUsage; }

    public Double getDiskUsage() { return diskUsage; }
    public void setDiskUsage(Double diskUsage) { this.diskUsage = diskUsage; }

    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}