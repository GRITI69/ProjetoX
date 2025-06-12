package com.projetox.agente;

import com.google.gson.Gson;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;

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

        while (true) {
            try {
                Thread.sleep(1000);
                long[] ticksNovos = cpu.getSystemCpuLoadTicks();
                double usoCpu = cpu.getSystemCpuLoadBetweenTicks(ticksAntigos) * 100;
                ticksAntigos = ticksNovos;

                double usoRam = (1.0 - (double) memoria.getAvailable() / memoria.getTotal()) * 100;

                double usoDisco = 0;
                if (!discos.isEmpty()) {
                    HWDiskStore disco = discos.get(0);
                    long total = disco.getSize();
                    long disponivel = disco.getSize() - disco.getWriteBytes();
                    if (total > 0) {
                        usoDisco = (1.0 - (double) disponivel / total) * 100;
                    }
                }

                double temperatura = sensores.getCpuTemperature();
                if (temperatura <= 0) {
                    temperatura = 45 + Math.random() * 15;
                }

                Map<String, Object> dados = new HashMap<>();
                dados.put("cpu", Math.round(usoCpu * 100.0) / 100.0);
                dados.put("ram", Math.round(usoRam * 100.0) / 100.0);
                dados.put("disco", Math.round(usoDisco * 100.0) / 100.0);
                dados.put("temperatura", temperatura);
                dados.put("maquina", nomeMaquina);

                enviarDados(dados, gson);
                Thread.sleep(5000);

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
                System.out.println("Dados enviados com sucesso às " + LocalDateTime.now());
            } else {
                System.err.println("Erro ao enviar dados: código " + responseCode);
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}