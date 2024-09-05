package com.example.Websocket;

public class SystemMetrics {
    private String cpuUsage;
    private String ramUsage;
    private String storageUsage;
    private String swapUsage;
     public String getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(String cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getRamUsage() {
        return ramUsage;
    }

    public void setRamUsage(String ramUsage) {
        this.ramUsage = ramUsage;
    }

    public String getStorageUsage() {
        return storageUsage;
    }

    public void setStorageUsage(String storageUsage) {
        this.storageUsage = storageUsage;
    }

    public String getSwapUsage() {
        return swapUsage;
    }

    public void setSwapUsage(String swapUsage) {
        this.swapUsage = swapUsage;
    }

    @Override
    public String toString() {
        return String.format("CPU Usage: %s, RAM Usage: %s, Storage Usage: %s, Swap Usage: %s",
                cpuUsage, ramUsage, storageUsage, swapUsage);
    }
}
