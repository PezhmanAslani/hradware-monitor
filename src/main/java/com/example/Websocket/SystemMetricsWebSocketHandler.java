package com.example.Websocket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SystemMetricsWebSocketHandler extends TextWebSocketHandler {

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        executorService.scheduleAtFixedRate(() -> {
            try {
                SystemMetrics metrics = new SystemMetrics();
                metrics.setCpuUsage(getCpuUsage());
                metrics.setRamUsage(getRamUsage());
                metrics.setStorageUsage(getStorageUsage());
                metrics.setSwapUsage(getSwapUsage());
                String json = objectMapper.writeValueAsString(metrics);
                session.sendMessage(new TextMessage(json));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS); 
    }

  

private String getCpuUsage() throws Exception {
    Process process = Runtime.getRuntime().exec("mpstat 1 1");
    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;
    StringBuilder output = new StringBuilder();
    while ((line = reader.readLine()) != null) {
        output.append(line).append("\n");
    }
    System.out.println("CPU Usage Output:\n" + output.toString());
    
    String[] lines = output.toString().split("\n");
    if (lines.length > 1) {
        String[] cpuUsageParts = lines[lines.length - 2].split("\\s+");
        return (100 - Double.parseDouble(cpuUsageParts[cpuUsageParts.length - 1])) + "%"; 
    }
    return "N/A";
}

    private String getRamUsage() throws Exception {
        Process process = Runtime.getRuntime().exec("free -m");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Mem:")) {
                String[] parts = line.split("\\s+");
                long total = Long.parseLong(parts[1]);
                long used = Long.parseLong(parts[2]);
                return (used * 100 / total) + "%";
            }
        }
        return "N/A";
    }

    private String getStorageUsage() throws Exception {
        Process process = Runtime.getRuntime().exec("df -h /");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("/")) {
                String[] parts = line.split("\\s+");
                return parts[4]; 
            }
        }
        return "N/A";
    }

    private String getSwapUsage() throws Exception {
        Process process = Runtime.getRuntime().exec("free -m");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Swap:")) {
                String[] parts = line.split("\\s+");
                long total = Long.parseLong(parts[1]);
                long used = Long.parseLong(parts[2]);
                return (used * 100 / total) + "%";
            }
        }
        return "N/A";
    }
}
