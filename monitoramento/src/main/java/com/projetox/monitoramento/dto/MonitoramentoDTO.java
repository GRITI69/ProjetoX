package com.projetox.monitoramento.dto;

public class MonitoramentoDTO {
    private Double cpu;
    private Double ram;
    private Double disco;
    private Double temperatura;
    private String maquina;

    public Double getCpu() { return cpu; }
    public void setCpu(Double cpu) { this.cpu = cpu; }

    public Double getRam() { return ram; }
    public void setRam(Double ram) { this.ram = ram; }

    public Double getDisco() { return disco; }
    public void setDisco(Double disco) { this.disco = disco; }

    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }

    public String getMaquina() { return maquina; }
    public void setMaquina(String maquina) { this.maquina = maquina; }
}
