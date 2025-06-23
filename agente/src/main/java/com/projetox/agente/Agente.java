package com.projetox.agente;
//java -jar target/agente-1.0-SNAPSHOT-jar-with-dependencies.jar
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

        // Controle de Disco
        Map<String, Long> discoTransferAnterior = new HashMap<>();
        long tempoAnterior = System.currentTimeMillis();
        for (HWDiskStore disco : discos) {
            disco.updateAttributes();
            discoTransferAnterior.put(disco.getName(), disco.getTransferTime());
        }

        // Controle da Temperatura simulada
        double temperaturaSimulada = sensores.getCpuTemperature();
        if (temperaturaSimulada <= 0) {
            temperaturaSimulada = 40 + Math.random() * 5;
        }

        while (true) {
            try {
                Thread.sleep(2000); // Intervalo de coleta

                // CPU
                long[] ticksNovos = cpu.getSystemCpuLoadTicks();
                double usoCpu = cpu.getSystemCpuLoadBetweenTicks(ticksAntigos) * 100;
                ticksAntigos = ticksNovos;

                // RAM
                double usoRam = (1.0 - (double) memoria.getAvailable() / memoria.getTotal()) * 100;

                // DISCO (Tempo de atividade em %)
                long tempoAtual = System.currentTimeMillis();
                long intervalo = tempoAtual - tempoAnterior;
                tempoAnterior = tempoAtual;

                double usoDisco = 0;
                for (HWDiskStore disco : hal.getDiskStores()) {
                    disco.updateAttributes();
                    long tempoTransferAtual = disco.getTransferTime();
                    long tempoTransferAntigo = discoTransferAnterior.getOrDefault(disco.getName(), 0L);

                    long tempoAtivo = tempoTransferAtual - tempoTransferAntigo;
                    discoTransferAnterior.put(disco.getName(), tempoTransferAtual);

                    double uso = (tempoAtivo / (double) intervalo) * 100;
                    if (uso > usoDisco) {
                        usoDisco = uso;
                    }
                }
                if (usoDisco > 100) usoDisco = 100;

                // TEMPERATURA
                double temperatura = sensores.getCpuTemperature();
                if (temperatura <= 0) {
                    // Oscilação normal
                    temperaturaSimulada += (Math.random() * 2 - 1); // Varia -1 a +1
                    if (temperaturaSimulada < 40) temperaturaSimulada = 40;
                    if (temperaturaSimulada > 50) temperaturaSimulada = 50;

                    // Pico ocasional
                    if (Math.random() > 0.92) {
                        temperaturaSimulada = 70;
                    }
                    // Resfriamento após pico
                    if (temperaturaSimulada >= 70) {
                        temperaturaSimulada = 45 + Math.random() * 5;
                    }

                    temperatura = temperaturaSimulada;
                }

                // Dados JSON
                Map<String, Object> dados = new HashMap<>();
                dados.put("cpu", Math.round(usoCpu * 100.0) / 100.0);
                dados.put("ram", Math.round(usoRam * 100.0) / 100.0);
                dados.put("disco", Math.round(usoDisco * 100.0) / 100.0);
                dados.put("temperatura", Math.round(temperatura * 100.0) / 100.0);
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
                System.out.println("Dados enviados com sucesso - " + LocalDateTime.now());
            } else {
                System.err.println("Erro ao enviar dados: código " + responseCode);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
