package com.projetox.agente;

import com.google.gson.Gson;
import oshi.SystemInfo;
import oshi.hardware.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agente {

    private static final String API_URL = "http://localhost:8081/api/monitoramentos";

    public static void main(String[] args) {

        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor cpu = hal.getProcessor();
        GlobalMemory memoria = hal.getMemory();
        Sensors sensores = hal.getSensors();
        List<HWDiskStore> discos = hal.getDiskStores();

        String nomeMaquina;
        try {
            nomeMaquina = InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            nomeMaquina = "Desconhecida";
        }

        long[] ticksAntigos = cpu.getSystemCpuLoadTicks();
        Gson gson = new Gson();

        // Disco - controle do tempo de atividade
        Map<String, Long> discoTransferAnterior = new HashMap<>();
        long tempoAnterior = System.currentTimeMillis();
        for (HWDiskStore disco : discos) {
            disco.updateAttributes();
            discoTransferAnterior.put(disco.getName(), disco.getTransferTime());
        }

        // Controle de temperatura simulada
        double temperaturaSimulada = sensores.getCpuTemperature();
        if (temperaturaSimulada <= 0) {
            temperaturaSimulada = 40 + Math.random() * 5;
        }

        boolean subindo = true;

        while (true) {
            try {
                Thread.sleep(4000);

                // CPU
                long[] ticksNovos = cpu.getSystemCpuLoadTicks();
                double usoCpu = cpu.getSystemCpuLoadBetweenTicks(ticksAntigos) * 100;
                ticksAntigos = ticksNovos;

                // RAM
                double usoRam = (1.0 - (double) memoria.getAvailable() / memoria.getTotal()) * 100;

                // DISCO - Tempo de atividade real
                long tempoAtual = System.currentTimeMillis();
                long intervalo = tempoAtual - tempoAnterior;
                tempoAnterior = tempoAtual;

                double usoDisco = 0;
                for (HWDiskStore disco : discos) {
                    disco.updateAttributes();
                    long tempoTransferencia = disco.getTransferTime();
                    long tempoAnteriorDisco = discoTransferAnterior.getOrDefault(disco.getName(), 0L);
                    long deltaTransfer = tempoTransferencia - tempoAnteriorDisco;
                    discoTransferAnterior.put(disco.getName(), tempoTransferencia);

                    double usoPercentualDisco = (intervalo > 0) ? (deltaTransfer * 100.0 / intervalo) : 0;
                    usoDisco += usoPercentualDisco;
                }

                if (usoDisco > 100) usoDisco = 100;

                // Temperatura
                double temperaturaReal = sensores.getCpuTemperature();
                if (temperaturaReal <= 0) {
                    if (subindo) {
                        temperaturaSimulada += Math.random() * 2;
                        if (temperaturaSimulada >= 70) {
                            subindo = false;
                        }
                    } else {
                        temperaturaSimulada -= Math.random() * 2;
                        if (temperaturaSimulada <= 42) {
                            subindo = true;
                        }
                    }
                    temperaturaReal = temperaturaSimulada;
                }

                // Dados JSON
                Map<String, Object> dados = new HashMap<>();
                dados.put("cpu", Math.round(usoCpu * 100.0) / 100.0);
                dados.put("ram", Math.round(usoRam * 100.0) / 100.0);
                dados.put("disco", Math.round(usoDisco * 100.0) / 100.0);
                dados.put("temperatura", Math.round(temperaturaReal * 100.0) / 100.0);
                dados.put("maquina", nomeMaquina);

                enviarDados(dados, gson);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void enviarDados(Map<String, Object> dados, Gson gson) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = gson.toJson(dados);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200 || responseCode == 201) {
                System.out.println("PROJETO X RODANDO " + LocalDateTime.now());
            } else {
                System.err.println("Erro ao enviar dados: código " + responseCode);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
